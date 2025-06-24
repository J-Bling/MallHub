package com.mall.portal.cache.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.*;
import com.mall.mbg.model.*;
import com.mall.portal.cache.ProductCacheService;
import com.mall.portal.dao.ProductDao;
import com.mall.portal.domain.model.product.ProductAlbums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 商品缓存设计 提高采用redis存储缓存
 * 可以设置 private Boolean allowProductCache; 配置进行对 大量商品进行缓存
 * redis维护 一个销量排行榜 和 一个新品排行榜 一天时间后自动重排行榜 或者后台调用重置排行榜
 * 根据allowProductCache来动态从数据库/缓存中获取商品 使用分布式锁防止缓存失效导致缓存穿透/雪崩 使用空值缓存防止缓存击穿
 * 根据allowProductCache来动态从数据库/缓存中获取ProductSubModel 商品附属关联模型
 */

@Service
public class ProductCacheServiceImpl implements ProductCacheService {

    @Autowired protected CounterRedisService counterRedisService;
    @Autowired private RedisService redisService;
    @Autowired private PmsProductMapper productMapper;
    @Autowired private PmsProductAttributeValueMapper attributeValueMapper;
    @Autowired private PmsProductAlbumRelationMapper albumRelationMapper;
    @Autowired private PmsAlbumMapper albumMapper;
    @Autowired private PmsAlbumPicMapper albumPicMapper;
    @Autowired private PmsSkuStockMapper skuStockMapper;
    @Autowired private PmsProductLadderMapper ladderMapper;
    @Autowired private PmsProductFullReductionMapper fullReductionMapper;
    @Autowired private ProductDao productDao;

    @Value("${cache.allow.product}")
    private Boolean allowProductCache;

    private final int defaultLockMaxExpired = 1000;
    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    private final Lock ProductSaleLock = new ReentrantLock();
    private final Lock ProductStockLock = new ReentrantLock();
    private final Lock SkuStockLock = new ReentrantLock();
    private final Lock SkuSale = new ReentrantLock();

    private static final NullProduct nullProduct = new NullProduct();
    private static final NummProductModel nullProductModel = new NummProductModel();
    private final Logger logger = LoggerFactory.getLogger(ProductCacheServiceImpl.class);



    @Override
    public void addProduct(long id){
        PmsProduct product = productMapper.selectByPrimaryKey(id);
        if (product!=null){
            counterRedisService.zAdd(CacheKeys.ProductSaleRank,CacheKeys.Field(id),product.getSale());
            counterRedisService.zAdd(CacheKeys.ProductNewRank,CacheKeys.Field(id),product.getCreateAt());
            if (allowProductCache!=null && allowProductCache){
                this.setProductCache(product);
            }
        }
    }

    @Override
    public void deleteRank(){
        redisService.del(CacheKeys.ProductNewRank);
        redisService.del(CacheKeys.ProductSaleRank);
    }

    @Override
    public List<Long> getSaleRankList(int offset, int limit) {
        List<Long> ProductIds = new ArrayList<>();
        Set<String> productIdset = counterRedisService.zReverseRange(CacheKeys.ProductSaleRank,offset,offset+limit-1);
        if (productIdset!=null && !productIdset.isEmpty()){
            for (String id : productIdset){
                ProductIds.add(Long.parseLong(id));
            }
            return ProductIds;
        }
        List<PmsProduct> productList = productDao.getMaxSaleProduct(offset,limit);
        if (productList!=null && !productList.isEmpty()){
            Map<String,Double> cacheMap = new HashMap<>();
            for (PmsProduct product : productList){
                ProductIds.add(product.getId());
                cacheMap.put(CacheKeys.Field(product.getId()),product.getSale().doubleValue());
            }
            counterRedisService.zAddAll(CacheKeys.ProductSaleRank,cacheMap);
        }
        return ProductIds;
    }

    @Override
    public List<Long> getNewRankList(int offset, int limit) {
        List<Long> productIds = new ArrayList<>();
        Set<String> productIdSet = counterRedisService.zReverseRange(CacheKeys.ProductNewRank,offset,offset+limit-1);
        if (productIdSet!=null && !productIdSet.isEmpty()){
            for (String id : productIdSet){
                productIds.add(Long.parseLong(id));
            }
            return productIds;
        }
        List<PmsProduct> productList = productDao.getMaxCreateProduct(offset,limit);
        if (productList!=null && !productList.isEmpty()){
            Map<String,Double> cacheMap = new HashMap<>();
            for (PmsProduct product : productList){
                cacheMap.put(CacheKeys.Field(product.getId()),product.getCreateAt().doubleValue());
                productIds.add(product.getId());
            }
            counterRedisService.zAddAll(CacheKeys.ProductNewRank,cacheMap);
        }
        return productIds;
    }

    @Override
    public PmsProduct getProduct(long id) {
        return allowProductCache==null || !allowProductCache ? this.getProductInDb(id) : this.getProductInCache(id);
    }

    @Override
    public PmsProduct getProductCache(long id) {
        return this.getProductInCache(id);
    }

    @Override
    public ProductSubModel getProductModel(long productId) {
        return allowProductCache==null || !allowProductCache ? this.getProductSubModelInDb(productId) : this.getProductModelCache(productId);
    }

    @Override
    public PmsSkuStock getSkuStock(long productId, long skuId) {
        return allowProductCache==null || !allowProductCache ? this.getSkuStockInDb(skuId) : this.getSkuStockInCache(productId,skuId);
    }

    @Override
    public List<PmsSkuStock> getSkuStockList(long productId) {
        return allowProductCache==null || !allowProductCache ? this.getSkuStockListInDb(productId) : this.getSkuStockListInCache(productId);
    }

    @Override
    public ProductStats getSkuStockStats(long skuId) {
        String key = CacheKeys.SkuStockStats(skuId);
        Map<String,String> map = counterRedisService.hGetAll(key);
        if (map==null || map.isEmpty()){
            PmsSkuStock stock = skuStockMapper.selectByPrimaryKey(skuId);
            if (stock==null){
                return null;
            }
            map = new HashMap<>();
            map.put(CacheKeys.Sale,stock.getSale().toString());
            map.put(CacheKeys.Stock,stock.getStock().toString());
            this.setSkuStockStats(skuId,map);
            return new ProductStats(stock.getSale(),stock.getStock());
        }
        return new ProductStats(Integer.parseInt(map.get(CacheKeys.Sale)),Integer.parseInt(map.get(CacheKeys.Stock)));
    }

    @Override
    public ProductStats getProductStats(long productId) {
        Map<String,String> map = counterRedisService.hGetAll(CacheKeys.ProductStats(productId));
        if (map==null || map.isEmpty()){
            PmsProduct product = productMapper.selectByPrimaryKey(productId);
            if (product!=null){
                map = new HashMap<>();
                map.put(CacheKeys.Sale,product.getSale().toString());
                map.put(CacheKeys.Stock,product.getStock().toString());
                counterRedisService.hSetAll(CacheKeys.ProductStats(productId),map);
                return new ProductStats(product.getSale(),product.getStock());
            }
            return null;
        }
        return new ProductStats(Integer.parseInt(map.get(CacheKeys.Sale)),Integer.parseInt(map.get(CacheKeys.Stock)));
    }

    @Override
    public void incrementProductSale(long productId, int delta) throws InterruptedException {
        String key = CacheKeys.ProductStats(productId);
        Boolean has = counterRedisService.hHasKey(key,CacheKeys.Sale);
        if (has!=null && has){
            counterRedisService.hInCr(key,CacheKeys.Sale,delta);
            return;
        }

        boolean succee = false;
        int tryCount = 1;

        do{
            boolean isLock = ProductSaleLock.tryLock(defaultLockMaxExpired,timeUnit);
            if (!isLock){
                Thread.sleep(defaultLockTime);
                tryCount ++ ;
                continue;
            }
            try{
                Boolean had = counterRedisService.hHasKey(key,CacheKeys.Sale);
                if (had!=null && had){
                    counterRedisService.hInCr(key,CacheKeys.Sale,delta);
                    return;
                }
                PmsProduct product = productMapper.selectByPrimaryKey(productId);
                if (product!=null){
                    counterRedisService.hInCr(key,CacheKeys.Sale,product.getSale());
                }
                return;
            }catch (Exception e){
                logger.error("设置缓存失败:{}",e.getMessage());
            } finally {
                ProductSaleLock.unlock();
                succee=true;
            }
        }while (tryCount<5 && !succee);
    }

    @Override
    public void incrementProductStock(long productId, int delta) throws InterruptedException {
        String key = CacheKeys.ProductStats(productId);
        Boolean has = counterRedisService.hHasKey(key,CacheKeys.Stock);
        if (has!=null && has){
            counterRedisService.hInCr(key,CacheKeys.Stock,delta);
            return;
        }
        for (int tryCount =1 ; tryCount <= 5; tryCount++){
            boolean isLock = ProductStockLock.tryLock(defaultLockMaxExpired,timeUnit);
            if (!isLock){
                Thread.sleep(Math.min(100*(1<<(tryCount-1)),defaultLockMaxExpired));
                continue;
            }
            try {
                Boolean had = counterRedisService.hHasKey(key,CacheKeys.Stock);
                if (had!=null && had){
                    counterRedisService.hInCr(key,CacheKeys.Stock,delta);
                    return;
                }
                PmsProduct product = productMapper.selectByPrimaryKey(productId);
                if (product!=null){
                    counterRedisService.hInCr(key,CacheKeys.Stock,product.getStock());
                }
                return;
            }catch (Exception e){
                logger.error("插入数据失败 ：{}",e.getMessage());
            }finally {
                ProductStockLock.unlock();
            }
        }
    }

    @Override
    public void incrementSkuStock(long productId, long skuId, int delta) throws InterruptedException {
        String key = CacheKeys.SkuStockStats(skuId);
        Boolean had = counterRedisService.hHasKey(key,CacheKeys.Stock);
        if (had!=null && had){
            counterRedisService.hInCr(key,CacheKeys.Stock,delta);
        }else {
            for (int tryCount =0 ; tryCount <= 5; tryCount++){
                boolean isLock = SkuStockLock.tryLock(defaultLockMaxExpired,timeUnit);
                if (!isLock){
                    Thread.sleep(Math.min(100*(1<<(tryCount-1)),defaultLockMaxExpired));
                    continue;
                }
                try{
                    had = counterRedisService.hHasKey(key,CacheKeys.Stock);
                    if (had!=null && had){
                        counterRedisService.hInCr(key,CacheKeys.Stock,delta);
                        break;
                    }
                    PmsSkuStock stock = skuStockMapper.selectByPrimaryKey(skuId);
                    if (stock!=null){
                        counterRedisService.hInCr(key,CacheKeys.Stock,stock.getStock());
                    }

                }catch (Exception e){
                    logger.error("修改sku库存失败 productId:{},skuId:{};{}",productId,skuId,e.getMessage());
                    return;
                }finally {
                    SkuStockLock.unlock();
                }
            }
        }
        this.incrementProductStock(productId,delta);
    }

    @Override
    public void incrementSkuSale(long productId , long skuId, int delta) throws InterruptedException {
        String key = CacheKeys.SkuStockStats(skuId);
        Boolean had = counterRedisService.hHasKey(key,CacheKeys.Sale);
        if (had!=null && had){
            counterRedisService.hInCr(key,CacheKeys.Sale,delta);
        }else {
            for (int tryCount =0 ; tryCount <= 5; tryCount++){
                boolean isLock = SkuSale.tryLock(defaultLockMaxExpired,timeUnit);
                if (!isLock){
                    Thread.sleep(Math.min(100*(1<<(tryCount-1)),defaultLockMaxExpired));
                    continue;
                }
                try{
                    had = counterRedisService.hHasKey(key,CacheKeys.Sale);
                    if (had!=null && had){
                        counterRedisService.hInCr(key,CacheKeys.Sale,delta);
                        break;
                    }
                    PmsSkuStock stock = skuStockMapper.selectByPrimaryKey(skuId);
                    if (stock!=null){
                        counterRedisService.hInCr(key,CacheKeys.Sale,stock.getSale());
                    }

                }catch (Exception e){
                    logger.error("修改sku销量失败 productId:{},skuId:{};{}",productId,skuId,e.getMessage());
                    return;
                }finally {
                    SkuStockLock.unlock();
                }
            }
        }
    }

    @Override
    public void deleteProduct(long id){
        Long len = counterRedisService.zRemove(CacheKeys.ProductSaleRank,CacheKeys.Field(id));
        if(len ==null || len <1){
            len = counterRedisService.zRemove(CacheKeys.ProductNewRank,CacheKeys.Field(id));
        }
        this.delProductCache(id);
        this.delProductModelCache(id);
        this.delSkuStock(id);
        this.delProductStats(id);
    }

    @Override
    public void delProductCache(long id) {
        redisService.del(CacheKeys.ProductKey(id));
    }

    @Override
    public void delProductModelCache(long productId) {
        redisService.del(CacheKeys.ProductModelKey(productId));
    }

    @Override
    public void delSkuStock(long productId) {
        redisService.del(CacheKeys.SkuStockHashKey(productId));
    }

    @Override
    public void delSkuStockCount(long skuId) {
        counterRedisService.del(CacheKeys.SkuStockStats(skuId));
    }

    @Override
    public void delProductStats(long productId) {
        counterRedisService.del(CacheKeys.ProductStats(productId));
    }

    @Override
    public void increaseSales(long id, int sales) {
        counterRedisService.zIncrementScore(CacheKeys.ProductSaleRank,CacheKeys.Field(id),sales);
    }





    private PmsProduct getProductInDb(long id){
        return productMapper.selectByPrimaryKey(id);
    }

    private PmsProduct getProductInCache(long id){
        String key = CacheKeys.ProductKey(id);
        PmsProduct product =(PmsProduct) redisService.get(key);
        if (product!=null){
            if (! (product instanceof NullProduct)){
                ProductStats stats = this.getProductStats(id);
                if (stats!=null){
                    product.setStock(stats.getStock());
                    product.setSale(stats.getSale());
                }
            }
            return product;
        }
        String lockKey =CacheKeys.ProductKeyLock(id);
        for (int i=0;i<retryCount;i++){
            Boolean isLock = redisService.setNX(lockKey,lockKey,defaultLockMaxExpired);
            if (isLock==null || !isLock){
                try {
                    Thread.sleep(Math.min(defaultLockTime*(1<<(i-1)),defaultLockMaxExpired));
                }catch (InterruptedException interruptedException){
                    logger.error("获取线程休眠失败:{}",interruptedException.getMessage());
                }
                continue;
            }
            try {
                product =(PmsProduct) redisService.get(key);
                if (product!=null){
                    return product instanceof NullProduct ? null : product;
                }
                product = productMapper.selectByPrimaryKey(id);
                redisService.set(key,product!=null ? product : nullProduct);
            }finally {
                redisService.del(lockKey);
            }
            break;
        }
        return product;
    }

    private void setSkuStockStats(long skuId,Map<String,String> map){
        String key = CacheKeys.SkuStockStats(skuId);
        counterRedisService.hSetAll(key,map);
    }


    private ProductSubModel getProductSubModelInDb(long productId){
        ProductSubModel productModel = new ProductSubModel();
        try {
            productModel.setProductId(productId);
            //获取商品属性
            PmsProductAttributeValueExample attributeValueExample = new PmsProductAttributeValueExample();
            attributeValueExample.createCriteria().andProductIdEqualTo(productId);
            List<PmsProductAttributeValue> attributeValueList = attributeValueMapper.selectByExample(attributeValueExample);
            productModel.setProductAttributeValueList(attributeValueList);
            //获取商品价格阶梯
            PmsProductLadderExample ladderExample = new PmsProductLadderExample();
            ladderExample.createCriteria().andProductIdEqualTo(productId);
            List<PmsProductLadder> ladderList = ladderMapper.selectByExample(ladderExample);
            productModel.setProductLadderList(ladderList);
            //获取商品减满
            PmsProductFullReductionExample reductionExample = new PmsProductFullReductionExample();
            reductionExample.createCriteria().andProductIdEqualTo(productId);
            List<PmsProductFullReduction> fullReductionList = fullReductionMapper.selectByExample(reductionExample);
            productModel.setProductFullReductionList(fullReductionList);
            //获取商品相册集
            PmsProductAlbumRelationExample albumRelationExample = new PmsProductAlbumRelationExample();
            albumRelationExample.createCriteria().andProductIdEqualTo(productId);
            List<PmsProductAlbumRelation> albumRelationList = albumRelationMapper.selectByExample(albumRelationExample);
            if (albumRelationList != null && !albumRelationList.isEmpty()) {
                PmsProductAlbumRelation albumRelation = albumRelationList.get(0);
                Long albumId = albumRelation.getAlbumId();
                //获取相册集
                PmsAlbum album = albumMapper.selectByPrimaryKey(albumId);
                if (album != null) {
                    ProductAlbums productAlbums = new ProductAlbums();
                    BeanUtils.copyProperties(album, productAlbums);
                    //获取图片
                    PmsAlbumPicExample albumPicExample = new PmsAlbumPicExample();
                    albumPicExample.createCriteria().andAlbumIdEqualTo(albumId);
                    List<PmsAlbumPic> albumPicList = albumPicMapper.selectByExample(albumPicExample);
                    productAlbums.setAlbumPicList(albumPicList);
                    productModel.setProductAlbums(productAlbums);
                }
            }
        }catch (Exception e){
            logger.error("查询productMode失败productId:{},原因:{}",productId,e.getMessage());
        }
        return productModel;
    }

    private ProductSubModel getProductModelCache(long productId){
        String key =CacheKeys.ProductModelKey(productId);
        ProductSubModel productModel = (ProductSubModel) redisService.get(key);
        if (productModel!=null){
            return productModel instanceof NummProductModel ? null : productModel;
        }
        for (int i=0;i<retryCount;i++){
            String lockKey = CacheKeys.ProductModelKeyLock(productId);
            Boolean isLock = redisService.setNX(lockKey,defaultNULL,defaultLockExpired);
            if (isLock==null || !isLock){
                try {
                    Thread.sleep(Math.min(defaultLockTime*(1<<(i-1)),1000));
                }catch (InterruptedException interruptedException){
                    logger.error("线程 休眠失败:{}",interruptedException.getMessage());
                }
                continue;
            }
            try {
                productModel = (ProductSubModel) redisService.get(key);
                if (productModel != null) {
                    return productModel instanceof NummProductModel ? null : productModel;
                }
                productModel = this.getProductSubModelInDb(productId);
                redisService.set(key, productModel == null ? nullProductModel : productModel);
            }finally {
                redisService.del(lockKey);
            }
            break;
        }
        return productModel;
    }

    private PmsSkuStock getSkuStockInDb(long skuId){
        return skuStockMapper.selectByPrimaryKey(skuId);
    }

    private PmsSkuStock getSkuStockInCache(long productId,long skuId){
        try {
            PmsSkuStock skuStock = (PmsSkuStock) redisService.hGet(CacheKeys.SkuStockHashKey(productId), CacheKeys.Field(skuId));
            if (skuStock != null) {
                ProductStats productStats = this.getSkuStockStats(skuId);
                if (productStats!=null){
                    skuStock.setStock(productStats.getStock());
                    skuStock.setSale(productStats.getSale());
                }
                return skuStock;
            }
            PmsSkuStockExample example = new PmsSkuStockExample();
            example.createCriteria().andProductIdEqualTo(productId);
            List<PmsSkuStock> skuStockList = skuStockMapper.selectByExample(example);
            if (skuStockList != null && !skuStockList.isEmpty()) {
                Map<String, PmsSkuStock> stringPmsSkuStockMap = new HashMap<>();
                for (PmsSkuStock stock : skuStockList) {
                    stringPmsSkuStockMap.put(CacheKeys.Field(stock.getId()), stock);
                }
                redisService.hSetAll(CacheKeys.SkuStockHashKey(productId), stringPmsSkuStockMap);
                for (PmsSkuStock stock : skuStockList) {
                    if (stock.getId().equals(skuId)) {
                        Map<String,String> map = new HashMap<>();
                        map.put(CacheKeys.Sale,stock.getSale().toString());
                        map.put(CacheKeys.Stock,stock.getStock().toString());
                        this.setSkuStockStats(skuId,map);
                        return stock;
                    }
                }
            }
        }catch (Exception e){
            logger.error("获取 库存信息失败 productId:{},skuId:{},{}",productId,skuId,e.getMessage());
        }
        return null;
    }

    private List<PmsSkuStock> getSkuStockListInDb(long productId){
        PmsSkuStockExample example = new PmsSkuStockExample();
        example.createCriteria().andProductIdEqualTo(productId);
        return skuStockMapper.selectByExample(example);
    }

    private List<PmsSkuStock> getSkuStockListInCache(long productId){
        List<PmsSkuStock> skuStockList = new ArrayList<>();
        try {
            Map<Object, Object> map = redisService.hGetAll(CacheKeys.SkuStockHashKey(productId));
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    PmsSkuStock stock = (PmsSkuStock) entry.getValue();
                    ProductStats productStats = this.getSkuStockStats(stock.getId());
                    if (productStats!=null){
                        stock.setSale(productStats.getSale());
                        stock.setStock(productStats.getStock());
                    }
                    skuStockList.add(stock);
                }
                return skuStockList;
            }

            PmsSkuStockExample example = new PmsSkuStockExample();
            example.createCriteria().andProductIdEqualTo(productId);
            skuStockList = skuStockMapper.selectByExample(example);
            if (skuStockList != null && !skuStockList.isEmpty()) {
                Map<String, PmsSkuStock> skuStockMap = new HashMap<>();
                for (PmsSkuStock stock : skuStockList) {
                    skuStockMap.put("" + stock.getId(), stock);
                }
                redisService.hSetAll(CacheKeys.SkuStockHashKey(productId), skuStockMap);
            }
        }catch (Exception e){
            logger.error("获取库存信息失败:productId:{},{}",productId,e.getMessage());
        }
        return skuStockList;
    }

    private void setProductCache(PmsProduct product){
        redisService.set(CacheKeys.ProductKey(product.getId()),product);
    }
}

class NullProduct extends PmsProduct{}
class NummProductModel extends ProductCacheService.ProductSubModel{}