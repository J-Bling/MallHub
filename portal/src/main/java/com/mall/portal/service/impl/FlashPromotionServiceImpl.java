package com.mall.portal.service.impl;

import com.mall.mbg.mapper.*;
import com.mall.portal.cache.FlashPromotionCacheService;
import com.mall.portal.domain.model.flash.*;
import com.mall.portal.service.FlashPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FlashPromotionServiceImpl implements FlashPromotionService {
    @Autowired private FlashPromotionCacheService promotionCacheService;
    @Autowired private SmsFlashBehaviorMapper behaviorMapper;
    @Autowired private SmsFlashSkuRelationMapper skuRelationMapper;
    @Autowired private SmsFlashProductRelationMapper productRelationMapper;
    @Autowired private SmsFlashProductSubscribeMapper productSubscribeMapper;
    @Autowired private SmsFlashSessionSubscribeMapper sessionSubscribeMapper;
    @Autowired private SmsFlashStockFlowMapper stockFlowMapper;




    @Override
    public FlashProductRelation getFlashProductRelation(long productId) {
        return null;
    }

    @Override
    public List<FlashProduct> getFlashProductList(long sessionId) {
        return Collections.emptyList();
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
}
