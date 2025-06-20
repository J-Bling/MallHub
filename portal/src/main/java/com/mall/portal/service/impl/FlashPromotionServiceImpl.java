package com.mall.portal.service.impl;

import com.mall.mbg.mapper.*;
import com.mall.mbg.model.*;
import com.mall.portal.cache.FlashPromotionCacheService;
import com.mall.portal.domain.model.flash.*;
import com.mall.portal.service.ConsumerService;
import com.mall.portal.service.FlashPromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlashPromotionServiceImpl implements FlashPromotionService {

    @Autowired private FlashPromotionCacheService promotionCacheService;
    @Autowired private ConsumerService consumerService;
    @Autowired private SmsFlashBehaviorMapper behaviorMapper;
    @Autowired private SmsFlashSkuRelationMapper skuRelationMapper;
    @Autowired private SmsFlashProductRelationMapper productRelationMapper;
    @Autowired private SmsFlashProductSubscribeMapper productSubscribeMapper;
    @Autowired private SmsFlashSessionSubscribeMapper sessionSubscribeMapper;
    @Autowired private SmsFlashStockFlowMapper stockFlowMapper;

    private final int tryCount = 5;
    private final long lockExpire = FlashPromotionCacheService.defaultLockExpired;
    private final Logger logger = LoggerFactory.getLogger(FlashPromotionServiceImpl.class);

    private String findProductRelationLockKey(long productRelationId){
        return "find-product-relation-lock:"+productRelationId;
    }
    private String findSkuRelationLock(long productRelationId){
        return "find-sku-relation-lock:"+ productRelationId;
    }
    private String findProductRelationStockLock(long productRelationId){
        return "find-productRelation-stock:"+productRelationId;
    }
    private String findSkuRelationStockLock(long productRelationId){
        return "find-sku-relation-stock-lock:"+productRelationId;
    }


    @Override
    public FlashProductRelation getFlashProductRelation(long productId) {
        Set<String> sessionIdStr = promotionCacheService.getCurrentSessionIds();
        long sessionId = this.getSessionId(sessionIdStr,productId);
        if (sessionId == 0){
            return null;
        }
        String productRelationIdStr = promotionCacheService.getProductRelationId(sessionId,productId);
        if (productRelationIdStr==null){
            return null;
        }
        long productRelationId = Long.parseLong(productRelationIdStr);
        SmsFlashProductRelation productRelation = this.getProductRelation(sessionId,productRelationId);
        FlashProductRelation flashProductRelation = new FlashProductRelation();
        BeanUtils.copyProperties(productRelation,flashProductRelation);
        flashProductRelation.setFlashStock(this.getProductRelationStock(flashProductRelation.getId()));
        flashProductRelation.setFlashPromotion(promotionCacheService.flashPromotion(flashProductRelation.getPromotionId()));
        flashProductRelation.setFlashSession(promotionCacheService.getSession(flashProductRelation.getPromotionId(),flashProductRelation.getSessionId()));
        flashProductRelation.setBuyCount(this.getCurrentMemberBuyCount(sessionId,productId));
        if (flashProductRelation.getIsSku()){
            List<SmsFlashSkuRelation> skuRelationList = this.getProductSkuRelationAll(flashProductRelation.getId());
            Map<Long,Integer> map = this.getSkuRelationStock(flashProductRelation.getId());
            for (SmsFlashSkuRelation skuRelation : skuRelationList){
                skuRelation.setFlashStock(map.get(skuRelation.getId()));
            }
            flashProductRelation.setFlashSkuRelationList(skuRelationList);
        }
        return flashProductRelation;
    }



    @Override
    public List<FlashProduct> getFlashProductList(long sessionId,int offset,int limit) {
        List<FlashProduct> flashProductList = new ArrayList<>();
        Set<String> productRelationIds = promotionCacheService.getProductRelationIds(sessionId,offset,limit);
        if (productRelationIds==null || productRelationIds.isEmpty()){
            return flashProductList;
        }
        List<SmsFlashProductRelation> productRelationList = promotionCacheService.flashProductRelationList(sessionId,productRelationIds);
        for (String productRelationIdStr : productRelationIds){
            long productRelationId = Long.parseLong(productRelationIdStr);
            SmsFlashProductRelation productRelation = this.filterFlashProductRelationList(productRelationId,productRelationList);
            if (productRelation==null){
                productRelation = this.getProductRelation(sessionId,productRelationId);
            }
            PmsProduct product = promotionCacheService.getProduct(productRelation.getProductId());
            FlashProduct flashProduct = new FlashProduct();
            BeanUtils.copyProperties(product,flashProduct);
            flashProduct.setFlashProductRelation(productRelation);
            flashProduct.setFlashSkuRelationList(this.getProductSkuRelationAll(productRelationId));
            flashProduct.setBuyCount(this.getCurrentMemberBuyCount(sessionId,flashProduct.getId()));
            flashProductList.add(flashProduct);
        }
        return flashProductList;
    }


    @Override
    public List<FlashPromotion> getStartFlashPromotion(byte type) {
        return Collections.emptyList();
    }

    @Override
    public List<FlashPromotion> getAllFlashPromotion() {
        return Collections.emptyList();
    }

    @Override
    public List<FlashPromotion> getPreparationFlashPromotion() {
        return Collections.emptyList();
    }

    @Override
    public boolean incrementProductStock(long flashProductRelationId, long flashSkuRelationId, int delta) {
        return false;
    }

    @Override
    public boolean subscribeFlashSession(long flashSessionId) {
        return false;
    }

    @Override
    public boolean subscribeFlashProduct(long productId) {
        return false;
    }

    @Override
    public boolean unSubscribeFlashSession(long flashSessionId) {
        return false;
    }

    @Override
    public boolean unSubscribeFlashProduct(long productId) {
        return false;
    }

    @Override
    public List<FlashSubscribeSessionHistory> getSubscribeSessionHistoryList(int offset, int limit) {
        return Collections.emptyList();
    }

    @Override
    public List<FlashSubscribeProductHistory> getSubscribeProductHistoryList(int offset, int limit) {
        return Collections.emptyList();
    }

    @Override
    public List<SmsFlashBehavior> getUserBehaviorList(long sessionId,long productId){
        UmsMember member = consumerService.getCurrentMember();
        SmsFlashBehaviorExample example = new SmsFlashBehaviorExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId())
                .andSessionIdEqualTo(sessionId)
                .andProductIdEqualTo(productId);
        return behaviorMapper.selectByExample(example);
    }




    long getSessionId(Set<String> sessionIdSet,long productId){
        if (sessionIdSet==null || sessionIdSet.isEmpty()){
            return 0;
        }
        for (String id : sessionIdSet){
            long sessionId = Long.parseLong(id);
            Boolean is = promotionCacheService.containProduct(sessionId,productId);
            if (is!=null && is){
                return sessionId;
            }
        }
        return 0;
    }

    private void threadSleep(int i){
        try{
            //毫秒
            int baseTime = 200;
            Thread.sleep(Math.min(lockExpire, baseTime * (1L <<(i-1))));
        }catch (InterruptedException interruptedException){
            logger.error("线程休眠失败:{}",interruptedException.getMessage());
        }
    }

    private SmsFlashProductRelation getProductRelation(long sessionId,long productRelationId){
        SmsFlashProductRelation productRelation = promotionCacheService.flashProductRelation(sessionId,productRelationId);
        if (productRelation!=null){
            return productRelation;
        }
        for (int i=0;i<tryCount;i++){
            String lock = findProductRelationLockKey(productRelationId);
            Boolean isLock = promotionCacheService.tryLock(lock,lockExpire);
            if (isLock==null || !isLock){
                this.threadSleep(i);
                continue;
            }
            try {
                productRelation = promotionCacheService.flashProductRelation(sessionId, productRelationId);
                if (productRelation != null) {
                    return productRelation;
                }
                productRelation = productRelationMapper.selectByPrimaryKey(productRelationId);
                if (productRelation != null) {
                    promotionCacheService.setProductRelationCache(sessionId, productRelation);
                }
                break;
            }finally {
                promotionCacheService.unLock(lock);
            }
        }
        return productRelation;
    }


    private List<SmsFlashSkuRelation> getProductSkuRelationAll(long productRelationId){
        List<SmsFlashSkuRelation> skuRelationList = promotionCacheService.flashSkuRelationList(productRelationId);
        if (skuRelationList!=null && !skuRelationList.isEmpty()){
            return skuRelationList;
        }
        for (int i=0;i<tryCount;i++){
            String lock = findSkuRelationLock(productRelationId);
            Boolean isLock = promotionCacheService.tryLock(lock,lockExpire);
            if (isLock==null || !isLock){
                threadSleep(i);
                continue;
            }
            try {
                skuRelationList = promotionCacheService.flashSkuRelationList(productRelationId);
                if (skuRelationList!=null && !skuRelationList.isEmpty()){
                    return skuRelationList;
                }
                SmsFlashSkuRelationExample example = new SmsFlashSkuRelationExample();
                example.createCriteria().andProductRelationIdEqualTo(productRelationId);
                skuRelationList = skuRelationMapper.selectByExample(example);
                if (skuRelationList!=null && !skuRelationList.isEmpty()){
                    Map<String,Object> skuRelationMap = new HashMap<>();
                    for (SmsFlashSkuRelation skuRelation : skuRelationList){
                        skuRelationMap.put(skuRelation.getId().toString(),skuRelation);
                    }
                    promotionCacheService.setSkuRelationCache(productRelationId,skuRelationMap);
                }
                break;
            }finally {
                promotionCacheService.unLock(lock);
            }
        }
        return skuRelationList;
    }

    private Integer getCurrentMemberBuyCount(long sessionId,long productId){
        UmsMember member = consumerService.getCurrentMember();
        int count = 0;
        if (member!=null){
            String c = promotionCacheService.getUserBuyCount(member.getId(),sessionId,productId);
            if (c!=null){
                count = Integer.parseInt(c);
            }else {
                SmsFlashBehaviorExample example = new SmsFlashBehaviorExample();
                example.createCriteria().andProductIdEqualTo(productId).andSessionIdEqualTo(sessionId);
                List<SmsFlashBehavior> behaviorList = behaviorMapper.selectByExample(example);
                count = behaviorList!=null ? behaviorList.size() : 0;
                promotionCacheService.incrementUserBuyCount(member.getId(),sessionId,productId,count);
            }
        }
        return count;
    }

    private Integer getProductRelationStock(long productRelationId){
        Integer count = promotionCacheService.productStockCount(productRelationId);
        if (count !=null){
            return count;
        }
        for (int i=0;i<tryCount;i++){
            String lock = findProductRelationStockLock(productRelationId);
            Boolean isLock = promotionCacheService.tryLock(lock,lockExpire);
            if (isLock==null || !isLock){
                threadSleep(i);
                continue;
            }
            try {
                count = promotionCacheService.productStockCount(productRelationId);
                if (count != null) {
                    return count;
                }
                SmsFlashProductRelation relation = productRelationMapper.selectByPrimaryKey(productRelationId);
                if (relation != null) {
                    count = relation.getFlashStock();
                    promotionCacheService.setProductRelationStockCache(productRelationId, count);
                }
            }finally {
                promotionCacheService.unLock(lock);
            }
            break;
        }
        return count;
    }


    private Integer getSkuRelationStock(long productRelationId,long skuRelationId){
        Integer count = promotionCacheService.skuStockCount(productRelationId,skuRelationId);
        if (count==null){
            count = this.getSkuRelationStock(productRelationId).get(skuRelationId);
        }
        return count;
    }


    private Map<Long,Integer> getSkuRelationStock(long productRelationId){
        Map<Long,Integer> map = promotionCacheService.skuStockCountMap(productRelationId);
        if (map!=null && !map.isEmpty()){
            return map;
        }
        for (int i=0;i<tryCount;i++){
            String lock = findSkuRelationStockLock(productRelationId);
            Boolean isLock = promotionCacheService.tryLock(lock,lockExpire);
            if (isLock==null || !isLock){
                threadSleep(i);
                continue;
            }
            try {
                map = promotionCacheService.skuStockCountMap(productRelationId);
                if (map!=null && !map.isEmpty()){
                    return map;
                }
                SmsFlashSkuRelationExample example = new SmsFlashSkuRelationExample();
                example.createCriteria().andProductRelationIdEqualTo(productRelationId);
                List<SmsFlashSkuRelation> skuRelationList = skuRelationMapper.selectByExample(example);
                if (skuRelationList!=null && !skuRelationList.isEmpty()){
                    map = new HashMap<>();
                    for (SmsFlashSkuRelation skuRelation : skuRelationList){
                        map.put(skuRelation.getId(),skuRelation.getFlashStock());
                    }
                }
                break;
            }finally {
                promotionCacheService.unLock(lock);
            }
        }
        return map;
    }

    private SmsFlashProductRelation filterFlashProductRelationList(long id,List<SmsFlashProductRelation> productRelationList){
        for (SmsFlashProductRelation relation : productRelationList){
            if (relation==null){
                continue;
            }
            if (id==relation.getId()){
                return relation;
            }
        }
        return null;
    }

}