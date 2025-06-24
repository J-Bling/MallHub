package com.mall.admin.productor;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CouponManage extends RabbitManage{
    /**
     * 删除一个 优惠券 缓存
     */
    void delCoupon(Long id,Boolean isAllUseType) throws JsonProcessingException;
    /**
     * 商品取消优惠券使用
     */
    void delCacheCouponProductRelation(Long productId,Long couponId) throws JsonProcessingException;
    /**
     * 商品类取消优惠券
     */
    void delCacheCouponProductCateRelation(Long productCateId,Long couponId) throws JsonProcessingException;
}
