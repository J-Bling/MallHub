package com.mall.portal.cache;


import com.mall.mbg.model.PmsProduct;

public interface FlashPromotionCacheService {
    /**
     *获取 product
     */
    PmsProduct getProduct(long productId);
    /**
     * 销量变化 skuId可以==0
     */
    void incrementProductSale(long product,int skuId,int delta);
    /**
     * 库存变化 skuId可以==0
     */
    void incrementProductStock(long product,int skuId,int delta);
}
