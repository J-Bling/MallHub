package com.mall.admin.cache;

import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.mall.mbg.model.SmsCouponProductRelation;

import java.util.List;

public interface CouponCacheManage {
    String count = "count";
    String useCount = "useCount";
    String receiveCount = "receiveCount";

    /**
     * 删除一个 优惠券 缓存
     */
    void delCoupon(Long id,Boolean isAllUseType);
    /**
     * 商品取消优惠券使用
     */
    void delCacheCouponProductRelation(Long productId,Long couponId);
    /**
     * 商品类取消优惠券
     */
    void delCacheCouponProductRelationAll(Long productId);
    /**
     *
     */
    void delCacheCouponProductCateRelation(Long productCateId,Long couponId);
    /**
     *
     */
    void delCacheCouponProductCateRelationAll(Long productCateId);
    /**
     * 增加 优惠券 缓存
     */
    void addCoupon(SmsCoupon coupon);
    /**
     * 增加 优惠券-商品关联
     */
    void addCouponProductRelationAll(List<SmsCouponProductRelation> relationList);
    /**
     * 增加 coupon-proCate关联
     */
    void addCouponCouponProductCategoryRelation(List<SmsCouponProductCategoryRelation> categoryRelationList);
}
