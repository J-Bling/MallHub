package com.mall.portal.domain.model.flash;

import com.mall.mbg.model.SmsFlashPromotion;
import com.mall.mbg.model.SmsFlashSession;
import com.mall.mbg.model.SmsFlashSessionSubscribe;

public class FlashSubscribeSessionHistory extends SmsFlashSessionSubscribe {
    private SmsFlashPromotion promotion;
    private SmsFlashSession session;

    public SmsFlashPromotion getPromotion() {
        return promotion;
    }

    public SmsFlashSession getSession() {
        return session;
    }

    public void setSession(SmsFlashSession session) {
        this.session = session;
    }

    public void setPromotion(SmsFlashPromotion promotion) {
        this.promotion = promotion;
    }
}
