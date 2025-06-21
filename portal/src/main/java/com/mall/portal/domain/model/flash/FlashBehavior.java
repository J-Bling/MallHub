package com.mall.portal.domain.model.flash;

import com.mall.mbg.model.*;

public class FlashBehavior extends SmsFlashBehavior{
     private SmsFlashProductRelation productRelation;
     private PmsProduct product;
     private SmsFlashPromotion flashPromotion;
     private SmsFlashSession flashSession;

    public PmsProduct getProduct() {
        return product;
    }

    public SmsFlashProductRelation getProductRelation() {
        return productRelation;
    }

    public SmsFlashPromotion getFlashPromotion() {
        return flashPromotion;
    }

    public SmsFlashSession getFlashSession() {
        return flashSession;
    }

    public void setProduct(PmsProduct product) {
        this.product = product;
    }

    public void setFlashPromotion(SmsFlashPromotion flashPromotion) {
        this.flashPromotion = flashPromotion;
    }

    public void setProductRelation(SmsFlashProductRelation productRelation) {
        this.productRelation = productRelation;
    }

    public void setFlashSession(SmsFlashSession flashSession) {
        this.flashSession = flashSession;
    }
}
