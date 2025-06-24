package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.mall.mbg.model.SmsCouponProductRelation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CouponCacheService extends Cache {
    String count = "count";
    String useCount = "useCount";
    String receiveCount = "receiveCount";
    /**
     * 获取优惠券信息
     */
    SmsCoupon get(long couponId);
    /**
     * 获取通用优惠券ids
     */
    List<Long> getAllUseTypeCouponIds();
    /**
     * 根据 id列表获取优惠券列表
     */
    List<SmsCoupon> get(List<Long> couponIds);
    /**
     * 根据商品id获取商品对优惠券的关联列表
     */
    List<SmsCouponProductRelation> getCouponProductRelationList(long productId);
    /**
     * 根据商品类型id获取商品类型对优惠券的关联列表
     */
    List<SmsCouponProductCategoryRelation> getCouponProductCategoryRelationList(long productCategoryId);


    void incrementCount(long couponId,long delta);
    void incrementUseCount(long couponId,long delta);
    void incrementReceiveCount(long couponId,long delta);


    class CouponStats{
        private final Map<String,String> stringStringMap = new HashMap<>();

        public CouponStats(){}
        public CouponStats(SmsCoupon coupon){
            stringStringMap.put(count,coupon.getCount()+"");
            stringStringMap.put(useCount,coupon.getUseCount()+"");
            stringStringMap.put(receiveCount,coupon.getReceiveCount()+"");
        }

        public static void refresh(Map<String,String> map,SmsCoupon coupon){
            coupon.setCount(Integer.parseInt(map.get(count)));
            coupon.setUseCount(Integer.parseInt(map.get(useCount)));
            coupon.setReceiveCount(Integer.parseInt(map.get(receiveCount)));
        }

        public Map<String, String> getStringStringMap() {
            return stringStringMap;
        }
    }
}
