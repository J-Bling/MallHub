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
import org.springframework.stereotype.Service;

import java.util.*;
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

    private final Lock ProductSaleLock = new ReentrantLock();
    private final Lock ProductStockLock = new ReentrantLock();
    private final static int dividend = 1000;
    private final static int newProductExpired = 259200;
    private final Logger logger = LoggerFactory.getLogger(ProductCacheServiceImpl.class);

    @Override
    public Boolean addProductInSaleRank(long id, double score) {
        return counterRedisService.zAdd(CacheKeys.ProductSaleRank,CacheKeys.Field(id),score);
    }

    @Override
    public Boolean addProductNewRank(long id) {
        this.addProductInSaleRank(id,0.0);
        return counterRedisService.zAdd(CacheKeys.ProductNewRank,CacheKeys.ProductKey(id), (double) System.currentTimeMillis() /dividend);
    }

    @Override
    public PmsProduct getProduct(long id) {
        return this.getProductInCache(id,1);
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
    public List<PmsProduct> geSaleRanktList(int offset, int limit) {
        List<PmsProduct> productList = new ArrayList<>();
        try {
            Set<String> productIds = counterRedisService.zReverseRange(CacheKeys.ProductSaleRank, offset, offset + limit - 1);
            if (productIds == null || productIds.isEmpty()) {
                productList = productDao.getProductOfSale(offset, limit);
                if (productIds != null && !productIds.isEmpty()) {
                    Map<String, Double> setMap = new HashMap<>();
                    for (PmsProduct product : productList) {
                        setMap.put("" + product.getId(), product.getSale().doubleValue());
                    }
                    counterRedisService.zAddAll(CacheKeys.ProductSaleRank, setMap);
                }
                return productList;
            }

            for (String id : productIds) {
                productList.add(this.getProduct(Long.parseLong(id)));
            }
        }catch (Exception e){
            logger.error("按销量查询商品失败:{}",e.getMessage());
        }
        return productList;
    }

    @Override
    public List<PmsProduct> getNewRankList(int offset, int limit) {
        Long len = counterRedisService.zSize(CacheKeys.ProductNewRank);
        if (len !=null && len>0 && len<=offset){
            return null;
        }
        List<PmsProduct> productList = new ArrayList<>();
        try {
            long date = this.getThreeDaysAgo();
            Set<String> productIds = counterRedisService.zRangeByScore(CacheKeys.ProductNewRank,
                    (double) date /dividend,
                    (double) System.currentTimeMillis() /dividend);

            if (productIds == null || productIds.isEmpty()){
                productList = productDao.getProductOfCreate(offset,limit,date);
                if (productList!=null && !productList.isEmpty()){
                    Map<String,Double> setmap = new HashMap<>();
                    for (PmsProduct product : productList){
                        setmap.put(""+product.getId(), (double) (System.currentTimeMillis()/dividend));
                    }
                    counterRedisService.zAddAll(CacheKeys.ProductNewRank,setmap);
                    counterRedisService.zRemoveRangeByScore(CacheKeys.ProductNewRank,0.0, (double) date /dividend);
                }
                return productList;
            }

            for (String id : productIds){
                productList.add(this.getProduct(Long.parseLong(id)));
            }

        }catch (Exception e){
            logger.error("按新品查询商品失败:{}",e.getMessage());
        }
        return productList;
    }

    private long getThreeDaysAgo(){
        return System.currentTimeMillis() - newProductExpired*dividend;
    }

    @Override
    public ProductModel getProductModel(long productId) {
        return this.getProductModelCache(productId,1);
    }

    @Override
    public PmsSkuStock getSkuStock(long productId, long skuId) {
        try {
            PmsSkuStock skuStock = (PmsSkuStock) redisService.hGet(CacheKeys.SkuStockHashKey(productId), CacheKeys.Field(skuId));
            if (skuStock != null) {
                Integer stock = this.getSkuStock(skuId);
                if (stock!=null){
                    skuStock.setStock(stock);
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
                        this.setSkuStockCount(skuId,stock.getStock());
                        return stock;
                    }
                }
            }
        }catch (Exception e){
            logger.error("获取 库存信息失败 productId:{},skuId:{},{}",productId,skuId,e.getMessage());
        }
        return null;
    }

    @Override
    public List<PmsSkuStock> getSkuStockList(long productId) {
        List<PmsSkuStock> skuStockList = new ArrayList<>();
        try {
            Map<Object, Object> map = redisService.hGetAll(CacheKeys.SkuStockHashKey(productId));
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    PmsSkuStock stock = (PmsSkuStock) entry.getValue();
                    Integer count = this.getSkuStock(stock.getId());
                    if (count!=null){
                        stock.setStock(count);
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

    private void setSkuStockCount(long skuId,int count){
        counterRedisService.hSet(CacheKeys.SkuStockCount,CacheKeys.Field(skuId),""+count);
    }

    @Override
    public Integer getSkuStock(long skuId) {
        String count = counterRedisService.hGet(CacheKeys.SkuStockCount,CacheKeys.Field(skuId));
        if (count==null){
            PmsSkuStock stock = skuStockMapper.selectByPrimaryKey(skuId);
            if (stock!=null){
                this.setSkuStockCount(skuId,stock.getStock());
            }
            return stock !=null ? stock.getStock() : null;
        }
        return Integer.parseInt(count);
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
    public void incrementProductSale(long productId, int delta) {
        String key = CacheKeys.ProductStats(productId);
        Boolean has = counterRedisService.hHasKey(key,CacheKeys.Sale);
        if (has!=null && has){
            counterRedisService.hInCr(key,CacheKeys.Sale,delta);
            return;
        }
        ProductSaleLock.lock();
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
        }finally {
            ProductSaleLock.unlock();;
        }
    }

    @Override
    public void incrementProductStock(long productId, int delta) {
        String key = CacheKeys.ProductStats(productId);
        Boolean has = counterRedisService.hHasKey(key,CacheKeys.Stock);
        if (has!=null && has){
            counterRedisService.hInCr(key,CacheKeys.Stock,delta);
            return;
        }
        ProductStockLock.lock();
        try{
            Boolean had = counterRedisService.hHasKey(key,CacheKeys.Stock);
            if (had!=null && had){
                counterRedisService.hInCr(key,CacheKeys.Stock,delta);
                return;
            }
            PmsProduct product = productMapper.selectByPrimaryKey(productId);
            if (product!=null){
                counterRedisService.hInCr(key,CacheKeys.Stock,product.getStock());
            }
        }finally {
            ProductStockLock.unlock();;
        }
    }

    @Override
    public void incrementSkuStock(long skuId, int delta) {
        counterRedisService.hInCr(CacheKeys.SkuStockCount,CacheKeys.Field(skuId),delta);
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

        productModel = new ProductModel();
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
            if (albumRelationList!=null && !albumRelationList.isEmpty()){
                PmsProductAlbumRelation albumRelation = albumRelationList.get(0);
                Long albumId = albumRelation.getAlbumId();
                //获取相册集
                PmsAlbum album = albumMapper.selectByPrimaryKey(albumId);
                if (album!=null){
                    ProductAlbums productAlbums = new ProductAlbums();
                    BeanUtils.copyProperties(album,productAlbums);
                    //获取图片
                    PmsAlbumPicExample albumPicExample = new PmsAlbumPicExample();
                    albumPicExample.createCriteria().andAlbumIdEqualTo(albumId);
                    List<PmsAlbumPic> albumPicList = albumPicMapper.selectByExample(albumPicExample);
                    productAlbums.setAlbumPicList(albumPicList);
                    productModel.setProductAlbums(productAlbums);
                }
            }

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
        counterRedisService.hDel(CacheKeys.SkuStockCount,CacheKeys.Field(skuId));
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
