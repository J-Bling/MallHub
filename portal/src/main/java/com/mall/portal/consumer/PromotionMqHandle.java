package com.mall.portal.consumer;

import com.mall.common.domain.ReSetPromotionModel;

public interface PromotionMqHandle {
    /**
     * 配置下一场次
     */
    void setNextPromotion(ReSetPromotionModel promotionModel);
    /**
     * 后台追加 product 总库存   sku-relation-id==0时只追加 总库存
     */
    void incrementProductStock(long productRelationId,long skuRelationId,int count);
    /**
     * 后台下架一个 product
     */
    void delProduct(long sessionId,long productId,long productRelationId);
    /**
     * 暂停一个 场次
     */
    void delSession(long sessionId);
}
