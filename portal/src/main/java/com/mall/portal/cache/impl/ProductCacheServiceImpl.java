package com.mall.portal.cache.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.*;
import com.mall.mbg.model.*;
import com.mall.portal.cache.ProductCacheService;
import com.mall.portal.dao.ProductDao;
import com.mall.portal.domain.model.ProductAlbums;
import io.lettuce.core.RedisException;
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
    private final int ProductLimit = 500;
    private final Logger logger = LoggerFactory.getLogger(ProductCacheServiceImpl.class);

    @Override
    public Boolean addProductInSaleRank(long id, double score) {
        return counterRedisService.zAdd(CacheKeys.ProductSaleRank,CacheKeys.Field(id),score);
    }

    @Override
    public Boolean addProductNewRank(long id) {
        this.addProductInSaleRank(id,0.0);
        return counterRedisService.zAdd(CacheKeys.ProductNewRank,CacheKeys.ProductKey(id), (double) System.currentTimeMillis());
    }

    @Override
    public PmsProduct getProduct(long id) {
        return allowProductCache==null || !allowProductCache ? this.getProductInDb(id) : this.getProductInCache(id,1);
    }

    @Override
    public PmsProduct getProductCache(long id) {
        return this.getProductInCache(id,1);
    }

    private PmsProduct getProductInDb(long id){
        return productMapper.selectByPrimaryKey(id);
    }

    private PmsProduct getProductInCache(long id ,int tryCount){
        PmsProduct product = null;
        if (tryCount >=retryCount){
            return null;
        }
        String key = CacheKeys.ProductKey(id);
        Object o =  redisService.get(key);
        if (defaultNULL.equals(o)){
            return product;
        }
        if (o!=null){
            return (PmsProduct) o;
        }

        String lockKey =CacheKeys.ProductKeyLock(id);
        Boolean isLock = redisService.setNX(lockKey,lockValue,defaultLockExpired);
        if (isLock==null || !isLock){
            try{
                Thread.sleep(defaultLockTime);
            }catch (InterruptedException e) {
                logger.error("线程休眠失败:{}",e.getMessage());
            }
            return this.getProductInCache(id,tryCount+1);
        }

        try {
            product = productMapper.selectByPrimaryKey(id);
            if (product != null) {
                redisService.set(key, product);
            }else {
                redisService.set(key,defaultNULL);
            }
            return product;
        }finally {
            redisService.del(lockKey);
        }
    }

    @Override
    public List<Long> geSaleRankList(int offset, int limit) {
        List<Long> ProductIds = new ArrayList<>();
        if (offset >ProductLimit){
            return ProductIds;
        }
        Long len = counterRedisService.zSize(CacheKeys.ProductSaleRank);
        if (len==null || len <= offset){
            List<PmsProduct> productList =productDao.getMaxSaleProduct(ProductLimit);
            if (productList==null || productList.isEmpty()){
                return ProductIds;
            }
            Map<String,Double> productRank = new HashMap<>();
            productList.stream().map(p->productRank.put(p.getId().toString(),p.getStock().doubleValue()));
            counterRedisService.zAddAll(CacheKeys.ProductSaleRank,productRank);
            if (offset>=productList.size()-1){
                return null;
            }
            for (int i = offset ; i<Math.min(limit,productList.size());i++){
                ProductIds.add(productList.get(i).getId());
            }
            return ProductIds;
        }
        Set<String> productIds = counterRedisService.zReverseRange(CacheKeys.ProductSaleRank, offset, offset + limit - 1);
        productIds.stream().map(id->ProductIds.add(Long.parseLong(id)));
        return ProductIds;
    }

    @Override
    public List<Long> getNewRankList(int offset, int limit) {
        Long len = counterRedisService.zSize(CacheKeys.ProductNewRank);
        List<Long> productIds = new ArrayList<>();
        if (offset >ProductLimit){
            return productIds;
        }
        if (len==null || len<= offset){
            List<PmsProduct> productList = productDao.getMaxCreateProduct(ProductLimit);
            if (productList==null || productList.isEmpty()){
                return productIds;
            }
            Map<String,Double> doubleMap = new HashMap<>();
            productList.stream().map(p->doubleMap.put(p.getId().toString(),p.getCreateAt().doubleValue()));
            counterRedisService.zAddAll(CacheKeys.ProductNewRank,doubleMap);
            if (productList.size() <= offset){
                return productIds;
            }
            for (int i = offset; i < Math.min(limit,productList.size());i++){
                productIds.add(productList.get(i).getId());
            }
            return productIds;
        }
        Set<String> strings = counterRedisService.zReverseRange(CacheKeys.ProductNewRank,offset,offset+limit-1);
        strings.stream().map(s->productIds.add(Long.parseLong(s)));
        return productIds;
    }


    @Override
    public ProductModel getProductModel(long productId) {
        return allowProductCache==null || !allowProductCache ? this.getProductModelInDb(productId) : this.getProductModelCache(productId,1);
    }

    @Override
    public PmsSkuStock getSkuStock(long productId, long skuId) {
        return allowProductCache==null || !allowProductCache ? this.getSkuStockInDb(skuId) : this.getSkuStockInCache(productId,skuId);
    }


    @Override
    public List<PmsSkuStock> getSkuStockList(long productId) {
        return allowProductCache==null || !allowProductCache ? this.getSkuStockListInDb(productId) : this.getSkuStockListInCache(productId);
    }

    private void setSkuStockStats(long skuId,Map<String,String> map){
        String key = CacheKeys.SkuStockStats(skuId);
        counterRedisService.hSetAll(key,map);
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
        if (map==null){
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
        String sale = map.get(CacheKeys.Sale);
        String stock = map.get(CacheKeys.Stock);
        if (stock !=null && sale !=null){
            return new ProductStats(Integer.parseInt(sale),Integer.parseInt(stock));
        }
        return null;
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

    private final Lock SkuStockLock = new ReentrantLock();
    private final Lock SkuSale = new ReentrantLock();

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
        this.incrementProductSale(productId,delta);
    }


    private ProductModel getProductModelInDb(long productId){
        ProductModel productModel = new ProductModel();
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

    private ProductModel getProductModelCache(long productId,int tryCount){
        if (tryCount>=retryCount){
            return null;
        }
        ProductModel productModel = null;
        String key =CacheKeys.ProductModelKey(productId);
        Object o = redisService.get(key);
        if (defaultNULL.equals(o)){
            return productModel;
        }
        if (o!=null){
            return (ProductModel) o;
        }

        String lockKey = CacheKeys.ProductModelKeyLock(productId);
        Boolean isLock = redisService.setNX(lockKey,defaultNULL,defaultLockExpired);
        if (isLock==null || !isLock){
            try {
                Thread.sleep(defaultLockTime);
            }catch (InterruptedException interruptedException){
                logger.error("线程 休眠失败:{}",interruptedException.getMessage());
            }
            return this.getProductModelCache(productId,tryCount+1);
        }
        try {
            productModel = this.getProductModelInDb(productId);
            redisService.set(key,productModel);

        }catch (RedisException redisException){
            logger.error("写入缓存失败:{}",redisException.getMessage());
        }catch (Exception e){
            logger.error("获取 productMode失败:{}",e.getMessage());
        } finally {
            redisService.del(lockKey);
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


    @Override
    public void delProductCache(long id) {
        try {
            redisService.del(CacheKeys.ProductKey(id));
        }catch (Exception e){
            logger.error("删除 product 失败:{}",e.getMessage());
        }
    }

    @Override
    public void delProductModelCache(long productId) {
        try {
            redisService.del(CacheKeys.ProductModelKey(productId));
        }catch (Exception e){
            logger.error("删除 productModel 失败:{}",e.getMessage());
        }
    }

    @Override
    public void delSkuStock(long productId) {
        try {
            redisService.del(CacheKeys.SkuStockHashKey(productId));
        }catch (Exception e){
            logger.error("删除 skuStock 缓存失败:{}",e.getMessage());
        }
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
        counterRedisService.incrementScore(CacheKeys.ProductSaleRank,CacheKeys.Field(id),sales);
    }
}