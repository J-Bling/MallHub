package com.mall.portal.cache.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.SmsFlashPromotionMapper;
import com.mall.mbg.mapper.SmsFlashSessionMapper;
import com.mall.mbg.model.*;
import com.mall.portal.cache.FlashPromotionCacheService;
import com.mall.portal.cache.ProductCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlashPromotionCacheServiceImpl implements FlashPromotionCacheService {
    @Autowired private RedisService redisService;
    @Autowired private CounterRedisService counterRedisService;
    @Autowired private ProductCacheService productCacheService;
    @Autowired private SmsFlashPromotionMapper promotionMapper;
    @Autowired private SmsFlashSessionMapper sessionMapper;

    private final Logger logger = LoggerFactory.getLogger(FlashPromotionCacheServiceImpl.class);

    private final long defaultExpire = 3600000L;
    private final long statsExpire = 259200000;

    @Override
    public PmsProduct getProduct(long productId) {
        return this.productCacheService.getProductCache(productId);
    }

    @Override
    public String getUserBuyCount(long userId, long sessionId, long productId) {
        return counterRedisService.get(CacheKeys.UserBuyCount(userId,sessionId,productId));
    }

    @Override
    public SmsFlashPromotion flashPromotion(long promotionId) {
        return  (SmsFlashPromotion) redisService.hGet(CacheKeys.PromotionKey,CacheKeys.Field(promotionId));
    }

    @Override
    public List<SmsFlashPromotion> flashPromotionList() {
        List<SmsFlashPromotion> promotionList = new ArrayList<>();
        Map<Object,Object> map = redisService.hGetAll(CacheKeys.PromotionKey);
        if (map!=null && !map.isEmpty()){
            for (Map.Entry<Object,Object> entry : map.entrySet()){
                promotionList.add((SmsFlashPromotion) entry.getValue());
            }
        }
        return promotionList;
    }

    @Override
    public Set<String> getCurrentSessionIds() {
        return counterRedisService.sMembers(CacheKeys.CurrentSessionSetKey);
    }

    @Override
    public Set<String> getNextSessionIds() {
        return counterRedisService.sMembers(CacheKeys.NextSessionSetKey);
    }

    @Override
    public Boolean containProduct(long sessionId, long productId) {
        return counterRedisService.sIsMember(CacheKeys.SessionProductSetKey(sessionId),CacheKeys.Field(productId));
    }

    @Override
    public String getProductRelationId(long sessionId, long productId) {
        return counterRedisService.hGet(CacheKeys.ProductIdToProductRelationId(sessionId),CacheKeys.Field(productId));
    }

    @Override
    public SmsFlashSession getSession(long promotionId) {
        return (SmsFlashSession) redisService.get(CacheKeys.SessionKey(promotionId));
    }

    @Override
    public Set<String> getProductRelationIds(long sessionId,int offset,int limit){
        return counterRedisService.zReverseRange(CacheKeys.ProductRelationZSetKey(sessionId),offset,offset+limit-1);
    }

    @Override
    public SmsFlashProductRelation flashProductRelation(long sessionId, long productRelationId) {
         return (SmsFlashProductRelation) redisService.hGet(CacheKeys.ProductRelationHashKey(sessionId),CacheKeys.Field(productRelationId));
    }

    @Override
    public List<SmsFlashProductRelation> flashProductRelationList(long sessionId,Set<String> strings) {
        return redisService.hGetAll(CacheKeys.ProductRelationHashKey(sessionId),strings)
                .stream()
                .map(s->(SmsFlashProductRelation) s)
                .collect(Collectors.toList());
    }

    @Override
    public SmsFlashSkuRelation flashSkuRelation(long productRelationId,long skuRelationId) {
        return (SmsFlashSkuRelation) redisService.hGet(CacheKeys.SkuStockRelationHashKey(productRelationId),CacheKeys.Field(skuRelationId));
    }

    @Override
    public List<SmsFlashSkuRelation> flashSkuRelationList(long productRelationId) {
        List<SmsFlashSkuRelation> relationList = new ArrayList<>();
        Map<Object,Object> map = redisService.hGetAll(CacheKeys.SkuStockRelationHashKey(productRelationId));
        if (map!=null && !map.isEmpty()){
            for (Map.Entry<Object,Object> entry : map.entrySet()){
                relationList.add((SmsFlashSkuRelation) entry.getValue());
            }
        }
        return relationList;
    }

    @Override
    public Integer productStockCount(long productRelationId) {
        String count = counterRedisService.get(CacheKeys.ProductFlashStockKey(productRelationId));
        return count !=null ? Integer.parseInt(count) : null;
    }

    @Override
    public Integer skuStockCount(long productRelationId, long skuRelationId) {
        String count = counterRedisService.hGet(CacheKeys.SkuFlashStockHashKey(productRelationId),CacheKeys.Field(skuRelationId));
        return count !=null ? Integer.parseInt(count) : null;
    }

    @Override
    public Map<Long, Integer> skuStockCountMap(long productRelationId) {
        Map<Long,Integer> result = new HashMap<>();
        Map<String,String> map=counterRedisService.hGetAll(CacheKeys.SkuFlashStockHashKey(productRelationId));
        if (map!=null && !map.isEmpty()){
            for (Map.Entry<String,String> entry : map.entrySet()){
                result.put(Long.parseLong(entry.getKey()),Integer.parseInt(entry.getValue()));
            }
        }
        return result;
    }

    @Override
    public Long incrementProductStock(long flashProductRelationId, long flashSkuRelationId, int delta) {
        Long total = counterRedisService.inCr(CacheKeys.ProductFlashStockKey(flashProductRelationId),delta);
        return total !=null && total > 0 && flashProductRelationId > 0L
                ? counterRedisService.hInCr(CacheKeys.SkuFlashStockHashKey(flashProductRelationId),CacheKeys.Field(flashSkuRelationId),delta)
                : total;
    }

    @Override
    public void incrementUserBuyCount(long userId, long sessionId, long productId, int delta) {
        String k = CacheKeys.UserBuyCount(userId,sessionId,productId);
        counterRedisService.inCr(k,delta);
        redisService.expire(k,defaultExpire);
    }

    @Override
    public void incrementProductSale(long sessionId,long productRelationId,long productId, long skuId, int delta) {
        try {
            productCacheService.incrementProductSale(productId, delta);
            if (skuId>0L){
                productCacheService.incrementSkuSale(productId,skuId,delta);
            }
            counterRedisService.zIncrementScore(CacheKeys.ProductRelationZSetKey(sessionId),CacheKeys.Field(productRelationId),delta);
        }catch (Exception e){
            logger.error("修改商品销量发送错误:{}",e.getMessage());
        }
    }

    @Override
    public void cleanSession(long sessionId) {
        boolean deled= counterRedisService.sRm(CacheKeys.CurrentSessionSetKey,CacheKeys.Field(sessionId));
        if (!deled){
            counterRedisService.sRm(CacheKeys.NextSessionSetKey,CacheKeys.Field(sessionId));
        }
        redisService.del(CacheKeys.SessionProductSetKey(sessionId));
        redisService.del(CacheKeys.ProductIdToProductRelationId(sessionId));
        redisService.del(CacheKeys.ProductRelationZSetKey(sessionId));
        redisService.del(CacheKeys.ProductRelationHashKey(sessionId));
    }

    @Override
    public void cleanPromotion(long promotionId) {
        redisService.hDel(CacheKeys.PromotionKey,CacheKeys.Field(promotionId));
        redisService.del(CacheKeys.SessionKey(promotionId));
    }




    @Override
    public void reSetPromotion(ReSetPromotionModel promotionModel) {
        if (promotionModel==null) {
            return;
        }
        //清空过期活动
        List<Long> endPromotionIds = promotionModel.getLastPromotionIds();
        if (endPromotionIds!=null && !endPromotionIds.isEmpty()){
            for (Long id : endPromotionIds){
                this.cleanPromotion(id);
            }
        }
        //设置 promotion 缓存
        List<Long> promotionIds = promotionModel.getCurrentPromotionIds();
        if (promotionIds!=null && !promotionIds.isEmpty()){
            SmsFlashPromotionExample promotionExample = new SmsFlashPromotionExample();
            promotionExample.createCriteria().andIdIn(promotionIds);
            List<SmsFlashPromotion> promotionList = promotionMapper.selectByExample(promotionExample);
            Map<String,SmsFlashPromotion> flashPromotionMap = new HashMap<>();
            for (SmsFlashPromotion promotion : promotionList){
                flashPromotionMap.put(""+promotion.getId(),promotion);
            }
            redisService.hSetAll(CacheKeys.PromotionKey,flashPromotionMap);
        }
        //清空过期场次
        List<Long> endSessionIds = promotionModel.getToBeEndSessionIds();
        if (endSessionIds!=null && !endSessionIds.isEmpty()){
            for (Long id : endSessionIds){
                this.cleanSession(id);
            }
        }
        //设置开始的场次缓存
        List<Long> startSessionIds = promotionModel.getToStartSessionIds();
        if (startSessionIds!=null && !startSessionIds.isEmpty()){
            counterRedisService.sAddAll(CacheKeys.CurrentSessionSetKey,
                    startSessionIds
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()));
            SmsFlashSessionExample sessionExample = new SmsFlashSessionExample();
            sessionExample.createCriteria().andIdIn(startSessionIds);
            List<SmsFlashSession> sessionList = sessionMapper.selectByExample(sessionExample);
            Map<Long,Map<String,SmsFlashSession>> longMapMap = new HashMap<>();
            for (SmsFlashSession session : sessionList){
                redisService.set(CacheKeys.SessionKey(session.getPromotionId()),session);
            }
        }
        //设置将要开始场次
        List<Long> nextSessionIds = promotionModel.getToNextSessionIds();
        if (nextSessionIds!=null && !nextSessionIds.isEmpty()){
            counterRedisService.sAddAll(CacheKeys.NextSessionSetKey,nextSessionIds
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()));
        }
        //设置场次上架的商品
        Map<Long,List<Long>> sessionIdToProductIdMap = promotionModel.getSessionIdToProductIds();
        if (sessionIdToProductIdMap!=null && !sessionIdToProductIdMap.isEmpty()){
            for (Map.Entry<Long,List<Long>> entry : sessionIdToProductIdMap.entrySet()){
                counterRedisService.sAddAll(CacheKeys.SessionProductSetKey(entry.getKey()),entry.getValue()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.toList()));
            }
        }
        //设置 productId 对应 productRelationId关联
        Map<Long,Map<Long,Long>> sessionIdForProductIdToProductRelationIdMap = promotionModel.getSessionIdToProductRelationIds();
        if (sessionIdForProductIdToProductRelationIdMap !=null && !sessionIdForProductIdToProductRelationIdMap.isEmpty()){
            for (Map.Entry<Long,Map<Long,Long>> entry : sessionIdForProductIdToProductRelationIdMap.entrySet()){
                counterRedisService.hSetAll(CacheKeys.ProductIdToProductRelationId(entry.getKey()),
                        entry.getValue()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        e->e.getKey().toString(),
                                        m->m.getValue().toString()
                                ))
                );
            }
        }
        //设置商品关联 与销量排行
        Map<Long,Map<Long,Double>> productRelationIdToScore = promotionModel.getProductRelationIdToScore();
        if (productRelationIdToScore!=null && !productRelationIdToScore.isEmpty()){
            for (Map.Entry<Long,Map<Long,Double>> entry : productRelationIdToScore.entrySet()){
                counterRedisService.zAddAll(CacheKeys.ProductRelationZSetKey(entry.getKey()),
                        entry.getValue()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        e->e.getKey().toString(),
                                        Map.Entry::getValue
                                ))
                        );
            }
        }
    }

    @Override
    public void setProductRelationCache(long sessionId, SmsFlashProductRelation productRelation) {
        redisService.hSet(CacheKeys.ProductRelationHashKey(sessionId),CacheKeys.Field(productRelation.getId()),productRelation,defaultExpire);
    }

    @Override
    public void setSkuRelationCache(long productRelationId, SmsFlashSkuRelation skuRelation) {
        redisService.hSet(CacheKeys.SkuStockRelationHashKey(productRelationId),CacheKeys.Field(skuRelation.getId()),skuRelation,defaultExpire);
    }

    @Override
    public void setSkuRelationCache(long productRelationId, Map<String, Object> skuMap) {
        redisService.hSetAll(CacheKeys.SkuStockRelationHashKey(productRelationId),skuMap);
    }

    @Override
    public void setProductRelationStockCache(long productRelationId, int count) {
        String key = CacheKeys.ProductFlashStockKey(productRelationId);
        counterRedisService.set(key,count+"");
        redisService.expire(key,statsExpire);
    }

    @Override
    public void setSkuRelationStockCache(long productRelationId,long skuRelationId, int count) {
        counterRedisService.hSet(CacheKeys.SkuFlashStockHashKey(productRelationId),CacheKeys.Field(skuRelationId),count+"",statsExpire);
    }

    @Override
    public void setSkuRelationStockCache(long productRelationId, Map<String, String> skuStockMap) {
        counterRedisService.hSetAll(CacheKeys.SkuFlashStockHashKey(productRelationId),skuStockMap,statsExpire);
    }

    @Override
    public void removedProduct(long sessionId, long productId, long productRelationId) {
        counterRedisService.sRm(CacheKeys.SessionProductSetKey(sessionId),CacheKeys.Field(productId));
        counterRedisService.zRemove(CacheKeys.ProductRelationZSetKey(sessionId),CacheKeys.Field(productRelationId));
        redisService.del(CacheKeys.SkuFlashStockHashKey(productRelationId));
        redisService.del(CacheKeys.SkuStockRelationHashKey(productRelationId));
        redisService.hDel(CacheKeys.ProductRelationHashKey(sessionId),CacheKeys.Field(productRelationId));
    }

    @Override
    public Boolean tryLock(String key, long expire) {
        return redisService.setNX(key,lockValue,expire);
    }

    @Override
    public Boolean tryLock(String key) {
        return this.tryLock(key,defaultLockExpired);
    }

    @Override
    public void unLock(String key) {
        redisService.del(key);
    }
}