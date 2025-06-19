package com.mall.portal.domain.model.flash;


import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.SmsFlashBehavior;
import com.mall.mbg.model.SmsFlashProductRelation;
import com.mall.mbg.model.SmsFlashSkuRelation;

import java.util.List;

/**
 * 秒杀商品
 */
public class FlashProduct extends PmsProduct {
    private SmsFlashBehavior flashBehavior;
    private SmsFlashProductRelation flashProductRelation;
    private List<SmsFlashSkuRelation> flashSkuRelationList;

    public List<SmsFlashSkuRelation> getFlashSkuRelationList() {
        return flashSkuRelationList;
    }

    public SmsFlashBehavior getFlashBehavior() {
        return flashBehavior;
    }

    public SmsFlashProductRelation getFlashProductRelation() {
        return flashProductRelation;
    }

    public void setFlashSkuRelationList(List<SmsFlashSkuRelation> flashSkuRelationList) {
        this.flashSkuRelationList = flashSkuRelationList;
    }

    public void setFlashBehavior(SmsFlashBehavior flashBehavior) {
        this.flashBehavior = flashBehavior;
    }

    public void setFlashProductRelation(SmsFlashProductRelation flashProductRelation) {
        this.flashProductRelation = flashProductRelation;
    }
}
