package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.mall.mbg.model.SmsCouponProductRelation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CouponCacheService extends Cache {
    String CouponKey = "coupon-key";
    String AllUseTypeCouponKey = "all-use-type-coupon-key";
    default String CouponProductRelationKey(long productId){ return  "coupon-product-relation-key:"+productId;}
    default String CouponProductCategoryRelationKey(long productId) {return  "coupon-product-category-relation-key:"+productId;}
    default String HashField(long id){return ""+id;}
    default String CouponStatsKey(long couponId){return "stats-coupon-key";}

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

    /**
     * 删除优惠券 缓存
     */
    void delCacheCoupon(long couponId);
    /**
     * 删除通用优惠券缓存记录
     */
    void delCacheAllUseTypeCoupon(long couponId,boolean isAllUseType);
    /**
     * 删除商品-优惠券关联(多对多) 缓存
     */
    void delCacheCouponProductRelation(long productId,long couponId);
    /**
     * 删除商品类型-优惠券(多对多) 缓存
     */
    void delCacheCouponProductCategoryRelation(long productCategoryId,long couponId);

    class CouponStats{
        public static final String count = "count";
        public static final String useCount = "useCount";
        public static final String receiveCount = "receiveCount";

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



    void incrementCount(long couponId,long delta);
    void incrementUseCount(long couponId,long delta);
    void incrementReceiveCount(long couponId,long delta);
}
