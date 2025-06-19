package com.mall.portal.domain.model.flash;

import com.mall.mbg.model.SmsFlashPromotion;
import com.mall.mbg.model.SmsFlashSession;

import java.util.List;

/**
 * 秒杀活动 获取当前活动场次 抢购的商品
 */
public class FlashPromotion extends SmsFlashPromotion {
    private SmsFlashSession flashSession;
    private List<FlashProduct> productList;

    public SmsFlashSession getFlashSession() {
        return flashSession;
    }

    public List<FlashProduct> getProductList() {
        return productList;
    }

    public void setFlashSession(SmsFlashSession flashSession) {
        this.flashSession = flashSession;
    }

    public void setProductList(List<FlashProduct> productList) {
        this.productList = productList;
    }
}
