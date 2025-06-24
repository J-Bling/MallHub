package com.mall.admin.cache.impl;

import com.mall.admin.cache.CouponCacheManage;
import com.mall.common.constant.enums.CouponUseTypeEnum;
import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.mall.mbg.model.SmsCouponProductRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouponCacheManageImpl implements CouponCacheManage {
    @Autowired private RedisService redisService;
    @Autowired private CounterRedisService counterRedisService;

    @Value("${redis.key.CouponKey:coupon-key}")
    private String CouponKey;
    @Value("${redis.key.AllUseTypeCouponKey:all-use-type-coupon-key}")
    private String AllUseTypeCouponKey;

    String HashField(long id){return ""+id;}
    String CouponStatsKey(long couponId){return "stats-coupon-key:"+couponId;}
    String CouponProductRelationKey(long productId){ return  "coupon-product-relation-key:"+productId;}
    String CouponProductCategoryRelationKey(long productId) {return  "coupon-product-category-relation-key:"+productId;}

    private final Logger logger = LoggerFactory.getLogger(CouponCacheManageImpl.class);


    @Override
    public void delCoupon(Long id, Boolean isAllUseType) {
        redisService.hDel(CouponKey,""+id);
        if (isAllUseType){
            redisService.hDel(AllUseTypeCouponKey,""+id);
        }
    }

    @Override
    public void delCacheCouponProductRelation(Long productId, Long couponId) {
        redisService.hDel(CouponProductRelationKey(productId),""+couponId);
    }

    @Override
    public void delCacheCouponProductRelationAll(Long productId){
        redisService.del(CouponProductRelationKey(productId));
    }

    @Override
    public void delCacheCouponProductCateRelation(Long productCateId, Long couponId) {
        redisService.hDel(CouponProductCategoryRelationKey(productCateId),""+couponId);
    }

    @Override
    public void delCacheCouponProductCateRelationAll(Long productCateId){
        redisService.del(CouponProductCategoryRelationKey(productCateId));
    }

    @Override
    public void addCoupon(SmsCoupon coupon) {
        redisService.hSet(CouponKey,""+coupon.getId(),coupon);
        final Map<String,String> stringStringMap = new HashMap<>();
        stringStringMap.put(count,coupon.getCount()+"");
        stringStringMap.put(useCount,coupon.getUseCount()+"");
        stringStringMap.put(receiveCount,coupon.getReceiveCount()+"");
        counterRedisService.hSetAll(CouponStatsKey(coupon.getId()),stringStringMap);
        if (CouponUseTypeEnum.ALL.getCode().equals(coupon.getUseType())){
            counterRedisService.hSet(AllUseTypeCouponKey,""+coupon.getId(),""+coupon.getId());
        }
    }

    @Override
    public void addCouponProductRelationAll(List<SmsCouponProductRelation> relationList) {
        for (SmsCouponProductRelation relation : relationList) {
            redisService.hSet(CouponProductRelationKey(relation.getProductId()),""+relation.getCouponId(),relation);
        }
    }

    @Override
    public void addCouponCouponProductCategoryRelation(List<SmsCouponProductCategoryRelation> categoryRelationList) {
        for (SmsCouponProductCategoryRelation categoryRelation : categoryRelationList) {
            redisService.hSet(CouponProductCategoryRelationKey(categoryRelation.getProductCategoryId()),""+categoryRelation.getCouponId(),categoryRelation);
        }
    }
}
