package com.mall.portal.domain.model;

import com.mall.mbg.model.OmsCartItem;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 购物车促销项目
 */
public class PromotionCartItem extends OmsCartItem {
    @ApiModelProperty("促销活动信息")
    private String promotionMessage;
    @ApiModelProperty("促销活动减去的金额，针对每个商品")
    private BigDecimal reduceAmount;
    @ApiModelProperty("剩余库存-锁定库存")
    private Integer realStock;
    @ApiModelProperty("购买商品赠送积分")
    private Integer integration;
    @ApiModelProperty("购买商品赠送成长值")
    private Integer growth;

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
}
