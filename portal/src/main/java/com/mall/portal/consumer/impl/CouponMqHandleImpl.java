package com.mall.portal.consumer.impl;

import com.mall.portal.cache.CouponCacheService;
import com.mall.portal.consumer.CouponMqHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponMqHandleImpl implements CouponMqHandle {
    @Autowired private CouponCacheService couponCacheService;

    @Override
    public void delCacheCoupon(long couponId, boolean isAllUseType) {
        couponCacheService.delCacheAllUseTypeCoupon(couponId,isAllUseType);
    }

    @Override
    public void delCacheCouponProductRelation(long productId, long couponId) {
        couponCacheService.delCacheCouponProductRelation(productId,couponId);
    }

    @Override
    public void delCacheCouponProductCategoryRelation(long productCategoryId, long couponId) {
        couponCacheService.delCacheCouponProductCategoryRelation(productCategoryId,couponId);
    }
}
