package com.mall.portal.cache.impl;

import com.mall.common.constant.enums.CouponUseTypeEnum;
import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.SmsCouponMapper;
import com.mall.mbg.mapper.SmsCouponProductCategoryRelationMapper;
import com.mall.mbg.mapper.SmsCouponProductRelationMapper;
import com.mall.mbg.model.*;
import com.mall.portal.cache.CouponCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponCacheServiceImpl implements CouponCacheService {
    @Autowired private RedisService redisService;
    @Autowired protected CounterRedisService counterRedisService;
    @Autowired private SmsCouponMapper couponMapper;
    @Autowired private SmsCouponProductRelationMapper productRelationMapper;
    @Autowired private SmsCouponProductCategoryRelationMapper categoryRelationMapper;

    private final Logger logger = LoggerFactory.getLogger(CouponCacheServiceImpl.class);

    @Override
    public SmsCoupon get(long couponId) {
        try {
            String field = HashField(couponId);
            Object result = redisService.hGet(CouponKey, field);
            if (CouponCacheService.defaultNULL.equals(result)) {
                return null;
            }
            if (result != null) {
                SmsCoupon coupon =(SmsCoupon) result;
                this.refreshCoupon(coupon);
                return coupon;
            }
            SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
            if (coupon !=null){
                if (CouponUseTypeEnum.ALL.getCode().equals(coupon.getUseType())){
                    setAllUseTypeCouponCache(coupon.getId());
                }
                redisService.hSet(CouponKey,field,coupon);
                this.setCouponStatsCache(coupon);
            }else {
                redisService.hSet(CouponKey,field,CouponCacheService.defaultNULL);
            }
            return coupon;
        }catch (Exception e){
            logger.error("查询优惠券失败 : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<SmsCoupon> get(List<Long> couponIds) {
        List<SmsCoupon> couponList = new ArrayList<>();
        try {
            List<String> ids = couponIds.stream().map(this::HashField).collect(Collectors.toList());
            List<Object> results = redisService.hGetAll(CouponKey, ids);

            for (int i = 0; i < results.size(); i++) {
                Object result = results.get(i);
                if (result == null) {
                    SmsCoupon coupon = this.get(couponIds.get(i));
                    couponList.add(coupon);
                    continue;
                }
                if (CouponCacheService.defaultNULL.equals(result)) {
                    couponList.add(null);
                    continue;
                }
                SmsCoupon coupon = (SmsCoupon) result;
                this.refreshCoupon(coupon);
                couponList.add(coupon);
            }
        }catch (Exception e){
            logger.error("批量获取优惠券失败 : {}",e.getMessage());
        }
        return couponList;
    }

    @Override
    public List<SmsCouponProductRelation> getCouponProductRelationList(long productId) {
        List<SmsCouponProductRelation> productRelationList = new ArrayList<>();
        String key = CouponProductRelationKey(productId);
        try {
            Map<Object, Object> result = redisService.hGetAll(key);
            if (result != null && !result.isEmpty()) {
                for (Map.Entry<Object, Object> entry : result.entrySet()) {
                    productRelationList.add((SmsCouponProductRelation) entry.getValue());
                }
            } else if (result == null) {
                SmsCouponProductRelationExample relationExample = new SmsCouponProductRelationExample();
                relationExample.createCriteria().andProductIdEqualTo(productId);
                productRelationList = productRelationMapper.selectByExample(relationExample);

                if (productRelationList == null || productRelationList.isEmpty()) {
                    //空缓存
                    redisService.hSetAll(key, Collections.emptyMap());
                } else {
                    Map<String, SmsCouponProductRelation> relationMap = new HashMap<>();
                    for (SmsCouponProductRelation relation : productRelationList) {
                        //设置缓存
                        relationMap.put(HashField(relation.getCouponId()), relation);
                    }
                    redisService.hSetAll(key, relationMap);
                }
            }
        }catch (Exception e){
            logger.error("获取商品-优惠券关系失败 : {}",e.getMessage());
        }
        return productRelationList;
    }

    @Override
    public List<SmsCouponProductCategoryRelation> getCouponProductCategoryRelationList(long productCategoryId) {
        List<SmsCouponProductCategoryRelation> categoryRelationList = new ArrayList<>();
        String key = CouponProductCategoryRelationKey(productCategoryId);
        try {
            Map<Object, Object> resultMap = redisService.hGetAll(key);
            if (resultMap != null && !resultMap.isEmpty()) {
                for (Map.Entry<Object, Object> entry : resultMap.entrySet()) {
                    categoryRelationList.add((SmsCouponProductCategoryRelation) entry.getValue());
                }
            } else if (resultMap == null) {
                SmsCouponProductCategoryRelationExample categoryRelationExample = new SmsCouponProductCategoryRelationExample();
                categoryRelationExample.createCriteria().andProductCategoryIdEqualTo(productCategoryId);
                categoryRelationList = categoryRelationMapper.selectByExample(categoryRelationExample);
                if (categoryRelationList == null || categoryRelationList.isEmpty()) {
                    redisService.hSetAll(key, Collections.emptyMap());
                } else {
                    Map<String, SmsCouponProductCategoryRelation> categoryRelationMap = new HashMap<>();
                    for (SmsCouponProductCategoryRelation categoryRelation : categoryRelationList) {
                        categoryRelationMap.put(HashField(categoryRelation.getCouponId()), categoryRelation);
                    }
                    redisService.hSetAll(key, categoryRelationMap);
                }
            }
        }catch (Exception e){
            logger.error("获取商品类型-优惠券关系失败 : {}",e.getMessage());
        }
        return categoryRelationList;
    }

    @Override
    public void delCacheCoupon(long couponId) {
        redisService.hDel(CouponKey,HashField(couponId));
        this.delCouponStats(couponId);
    }

    @Override
    public void delCacheAllUseTypeCoupon(long couponId,boolean isAllUseType){
        this.delCacheCoupon(couponId);
        if (isAllUseType) {
            redisService.hDel(AllUseTypeCouponKey, HashField(couponId));
        }
    }

    @Override
    public void delCacheCouponProductRelation(long productId , long couponId) {
        String key = CouponProductRelationKey(productId);
        String field = HashField(couponId);
        redisService.hDel(key,field);
    }

    @Override
    public void delCacheCouponProductCategoryRelation(long productCategoryId , long couponId) {
        String key = CouponProductCategoryRelationKey(productCategoryId);
        String field = HashField(couponId);
        redisService.hDel(key,field);
    }

    private void setAllUseTypeCouponCache(long couponId){
        redisService.hSet(AllUseTypeCouponKey,HashField(couponId),couponId);
    }

    @Override
    public List<Long> getAllUseTypeCouponIds(){
        List<Long> couponIds = new ArrayList<>();
        Map<Object, Object> idMap = redisService.hGetAll(AllUseTypeCouponKey);
        if (idMap == null || idMap.isEmpty()){
            return couponIds;
        }
        for (Map.Entry<Object,Object> entry : idMap.entrySet()){
            long id = (long) entry.getValue();
            couponIds.add(id);
        }
        return couponIds;
    }

    private void setCouponStatsCache(SmsCoupon coupon){
        CouponStats stats = new CouponStats(coupon);
        counterRedisService.hSetAll(CouponStatsKey(coupon.getId()),stats.getStringStringMap());
    }

    private Map<String,String> getCouponStatsCache(long couponId){
        return counterRedisService.hGetAll(CouponStatsKey(couponId));
    }

    private void refreshCoupon(SmsCoupon coupon){
        Map<String,String> map = this.getCouponStatsCache(coupon.getId());
        if (map==null || map.isEmpty()){
            return;
        }
        CouponStats.refresh(map,coupon);
    }


    @Override
    public void incrementCount(long couponId,long delta){
        counterRedisService.hInCr(CouponStatsKey(couponId),CouponStats.count,delta);
    }

    @Override
    public void incrementUseCount(long couponId,long delta){
        counterRedisService.hInCr(CouponStatsKey(couponId),CouponStats.useCount,delta);
    }

    @Override
    public void incrementReceiveCount(long couponId,long delta){
        counterRedisService.hInCr(CouponStatsKey(couponId),CouponStats.receiveCount,delta);
    }

    private void delCouponStats(long couponId){
        redisService.del(CouponStatsKey(couponId));
    }
}