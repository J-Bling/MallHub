package com.mall.portal.consumer.impl;

import com.mall.common.domain.ReSetPromotionModel;
import com.mall.portal.cache.FlashPromotionCacheService;
import com.mall.portal.consumer.PromotionMqHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionMqHandleImpl implements PromotionMqHandle {
    @Autowired private FlashPromotionCacheService promotionCacheService;

    @Override
    public void setNextPromotion(ReSetPromotionModel promotionModel) {
        promotionCacheService.reSetPromotion(promotionModel);
    }

    @Override
    public void incrementProductStock(long productRelationId, long skuRelationId, int count) {
        promotionCacheService.incrementProductStock(productRelationId,skuRelationId,count);
    }

    @Override
    public void delProduct(long sessionId, long productId, long productRelationId) {
        promotionCacheService.removedProduct(sessionId,productId,productRelationId);
    }


    @Override
    public void delSession(long sessionId){
        promotionCacheService.cleanSession(sessionId);
    }
}
