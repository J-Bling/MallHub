package com.mall.admin.productor;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductManage extends RabbitManage{
    /**
     * 后台控制删除排行榜
     */
    void delRank() throws JsonProcessingException;
    /**
     * 后台通知前台上架一个商品
     */
    void addProduct(long productId) throws JsonProcessingException;
    /**
     * 后台通知下架一个商品
     */
    void deleteProduct(long productId) throws JsonProcessingException;
    /**
     * 后台修改一些该商品信息 需要前台删除该商品缓存
     */
    void upToDelProductCache(long productId) throws JsonProcessingException;
    /**
     * 后台修改了该商品附带信息需要 前台删除productSubModel缓存
     */
    void upToDelProductSubModelCache(long productId) throws JsonProcessingException;
    /**
     * 后台修改了该商品sku信息需要 前台删除所有相关skuStock缓存
     */
    void upToDelSkuStockCache(long productId) throws JsonProcessingException;
    /**
     * 后台修改该商品库存信息 前台删除相关统计数据 总库存 分库存
     */
    void upToDelStats(long productId,long skuId) throws JsonProcessingException;
}
