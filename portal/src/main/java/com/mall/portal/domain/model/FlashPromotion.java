package com.mall.portal.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FlashPromotion implements Serializable {
    //本场开始时间
    private Date startTime;
    //本场结束时间
    private Date endTime;
    //下场开始时间
    private Date nextStartTime;
    //下场结束的时间
    private Date nextEndTime;
    // 秒杀的商品
    private List<FlashPromotionProduct> productList;

    public Date getEndTime() {
        return endTime;
    }

    public Date getNextEndTime() {
        return nextEndTime;
    }

    public Date getNextStartTime() {
        return nextStartTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public List<FlashPromotionProduct> getProductList() {
        return productList;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setNextEndTime(Date nextEndTime) {
        this.nextEndTime = nextEndTime;
    }

    public void setNextStartTime(Date nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    public void setProductList(List<FlashPromotionProduct> productList) {
        this.productList = productList;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
