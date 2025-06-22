package com.mall.common.domain;


public class RabbitRpcPromotionMessage extends RabbitRpcMessage{
    private ReSetPromotionModel promotionModel;
    private Long sessionId;
    private Long productId;
    private Long productRelationId;
    private Long skuRelationId;
    private Integer count;

    public static RabbitRpcPromotionMessage Builder(){
        return new RabbitRpcPromotionMessage();
    }

    public RabbitRpcPromotionMessage addPromotionModel(ReSetPromotionModel model){
        this.promotionModel = model;
        return this;
    }

    public RabbitRpcPromotionMessage addSessionId(Long sessionId){
        this.sessionId=sessionId;
        return this;
    }

    public RabbitRpcPromotionMessage addProductId(Long productId){
        this.productId = productId;
        return this;
    }

    public RabbitRpcPromotionMessage addProductRelationId(Long productRelationId){
        this.productRelationId = productRelationId;
        return this;
    }

    public RabbitRpcPromotionMessage addSkuRelationId(Long skuRelationId){
        this.skuRelationId= skuRelationId;
        return this;
    }

    public RabbitRpcPromotionMessage addCount(Integer count){
        this.count=count;
        return this;
    }

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
