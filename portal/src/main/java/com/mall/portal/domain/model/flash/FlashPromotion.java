package com.mall.portal.domain.model.flash;

import com.mall.mbg.model.SmsFlashPromotion;
import com.mall.mbg.model.SmsFlashSession;

import java.util.List;

/**
 * 秒杀活动 获取当前活动场次 抢购的商品
 */
public class FlashPromotion extends SmsFlashPromotion{
    private List<SmsFlashSession> sessionList;
    private List<FlashProduct> productList;//当前场次的 product

    public List<SmsFlashSession> getSessionList() {
        return sessionList;
    }

    public List<FlashProduct> getProductList() {
        return productList;
    }

    public void setSessionList(List<SmsFlashSession> sessionList) {
        this.sessionList = sessionList;
    }

    public void setProductList(List<FlashProduct> productList) {
        this.productList = productList;
    }
}
