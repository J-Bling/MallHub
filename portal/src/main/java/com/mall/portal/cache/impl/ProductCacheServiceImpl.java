package com.mall.portal.cache.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.*;
import com.mall.mbg.model.*;
import com.mall.portal.cache.ProductCacheService;
import com.mall.portal.domain.model.ProductAlbums;
import io.lettuce.core.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    private final static int dividend = 1000;
    private final Logger logger = LoggerFactory.getLogger(ProductCacheServiceImpl.class);

    @Override
    public Boolean addProductInSaleRank(long id, double score) {
        return counterRedisService.zAdd(CacheKeys.ProductSaleRank,CacheKeys.Field(id),score);
    }

    @Override
    public Boolean addProductNewRank(long id) {
        Boolean status = this.addProductInSaleRank(id,0.0);
        status = counterRedisService.zAdd(CacheKeys.ProductNewRank,CacheKeys.ProductKey(id), (double) System.currentTimeMillis() /dividend);
        return status;
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
        return Collections.emptyList();
    }

    @Override
    public List<PmsProduct> getNewRankList(int offset, int limit) {
        return Collections.emptyList();
    }

    @Override
    public ProductModel getProductModel(long productId) {
        return this.getProductModelCache(productId,1);
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
            //获取商品库存
            PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
            skuStockExample.createCriteria().andProductIdEqualTo(productId);
            List<PmsSkuStock> skuStockList = skuStockMapper.selectByExample(skuStockExample);
            productModel.setSkuStockList(skuStockList);
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
            redisService.del(CacheKeys.ProductModelKey(id));
        }catch (Exception e){
            logger.error("删除失败:{}",e.getMessage());
        }
    }

    @Override
    public void increaseSales(long id, int sales) {
        counterRedisService.incrementScore(CacheKeys.ProductSaleRank,CacheKeys.Field(id),sales);
    }
}
