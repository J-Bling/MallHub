package com.mall.portal.domain.model;

import com.mall.mbg.model.PmsProduct;

import java.math.BigDecimal;

/**
 * 秒杀商品
 */
public class FlashPromotionProduct extends PmsProduct {
    //秒杀价格
    private BigDecimal flashPromotionPrice;
    //用于秒杀到数量
    private Integer flashPromotionCount;
    //秒杀限购2
    private Integer flashPromotionLimit;

    public Integer getFlashPromotionCount() {
        return flashPromotionCount;
    }

    public Integer getFlashPromotionLimit() {
        return flashPromotionLimit;
    }

    public BigDecimal getFlashPromotionPrice() {
        return flashPromotionPrice;
    }

    public void setFlashPromotionCount(Integer flashPromotionCount) {
        this.flashPromotionCount = flashPromotionCount;
    }

    public void setFlashPromotionLimit(Integer flashPromotionLimit) {
        this.flashPromotionLimit = flashPromotionLimit;
    }

    public void setFlashPromotionPrice(BigDecimal flashPromotionPrice) {
        this.flashPromotionPrice = flashPromotionPrice;
    }
}
