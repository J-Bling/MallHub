package com.mall.portal.consumer;

public interface CouponMqHandle {
    void delCacheCoupon(long couponId,boolean isAllUseType);
    void delCacheCouponProductRelation(long productId,long couponId);
    void delCacheCouponProductCategoryRelation(long productCategoryId,long couponId);
}
