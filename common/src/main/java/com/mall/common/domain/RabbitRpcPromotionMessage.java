package com.mall.common.domain;


public class RabbitRpcPromotionMessage extends RabbitRpcMessage{
    private ReSetPromotionModel promotionModel;
    private Long sessionId;
    private Long productId;
    private Long productRelationId;
    private Long skuRelationId;
    private Integer count;

    public Long getProductId() {
        return productId;
    }

    public Integer getCount() {
        return count;
    }

    public Long getProductRelationId() {
        return productRelationId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getSkuRelationId() {
        return skuRelationId;
    }

    public ReSetPromotionModel getPromotionModel() {
        return promotionModel;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setProductRelationId(Long productRelationId) {
        this.productRelationId = productRelationId;
    }

    public void setPromotionModel(ReSetPromotionModel promotionModel) {
        this.promotionModel = promotionModel;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setSkuRelationId(Long skuRelationId) {
        this.skuRelationId = skuRelationId;
    }
}
