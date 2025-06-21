package com.mall.portal.domain.model.flash;

import com.mall.mbg.model.*;

public class FlashSubscribeProductHistory  extends SmsFlashProductSubscribe{
    private PmsProduct product;
    private SmsFlashSession session;
    private SmsFlashPromotion promotion;
    private SmsFlashProductRelation flashProductRelation;
    private SmsFlashSkuRelation flashSkuRelation;

    public SmsFlashSession getSession() {
        return session;
    }

    public SmsFlashPromotion getPromotion() {
        return promotion;
    }

    public PmsProduct getProduct() {
        return product;
    }

    public SmsFlashProductRelation getFlashProductRelation() {
        return flashProductRelation;
    }

    public SmsFlashSkuRelation getFlashSkuRelation() {
        return flashSkuRelation;
    }

    public void setPromotion(SmsFlashPromotion promotion) {
        this.promotion = promotion;
    }

    public void setSession(SmsFlashSession session) {
        this.session = session;
    }

    public void setProduct(PmsProduct product) {
        this.product = product;
    }

    public void setFlashProductRelation(SmsFlashProductRelation flashProductRelation) {
        this.flashProductRelation = flashProductRelation;
    }

    public void setFlashSkuRelation(SmsFlashSkuRelation flashSkuRelation) {
        this.flashSkuRelation = flashSkuRelation;
    }
}
