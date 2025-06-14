package com.mall.portal.domain.model;

import com.mall.mbg.model.OmsCartItem;

import java.math.BigDecimal;

public class CartPromotionItem extends OmsCartItem {
    //"促销活动信息"
    private String promotionMessage;
    //"促销活动减去的金额，针对每个商品"
    private BigDecimal reduceAmount;
    //"剩余库存-锁定库存"
    private Integer realStock;
    //"购买商品赠送积分"
    private Integer integration;
    //"购买商品赠送成长值"
    private Integer growth;

    public Integer getGrowth() {
        return growth;
    }

    public Integer getIntegration() {
        return integration;
    }

    public Integer getRealStock() {
        return realStock;
    }

    public String getPromotionMessage() {
        return promotionMessage;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public BigDecimal getReduceAmount() {
        return reduceAmount;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public void setRealStock(Integer realStock) {
        this.realStock = realStock;
    }

    public void setPromotionMessage(String promotionMessage) {
        this.promotionMessage = promotionMessage;
    }

    public void setReduceAmount(BigDecimal reduceAmount) {
        this.reduceAmount = reduceAmount;
    }
}
