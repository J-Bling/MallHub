package com.mall.portal.domain.model;

import com.mall.mbg.model.OmsCartItem;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;


public class PromotionCartItem implements Serializable {
    private Long cartId;
    //促销活动信息
    private String promotionMessage;
    //计算促销活动后减免的金额
    private BigDecimal reduceAmount;
    //剩余库存-锁定库存
    private Integer realStock;
    //购买商品赠送积分
    private Integer integration;
    //购买商品赠送成长值
    private Integer growth;

    public Long getCartId() {
        return cartId;
    }

    public String getPromotionMessage() {
        return promotionMessage;
    }

    public BigDecimal getReduceAmount() {
        return reduceAmount;
    }

    public Integer getRealStock() {
        return realStock;
    }

    public Integer getIntegration() {
        return integration;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setPromotionMessage(String promotionMessage) {
        this.promotionMessage = promotionMessage;
    }

    public void setRealStock(Integer realStock) {
        this.realStock = realStock;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void setReduceAmount(BigDecimal reduceAmount) {
        this.reduceAmount = reduceAmount;
    }
}
