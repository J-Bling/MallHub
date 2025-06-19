package com.mall.portal.domain.model.flash;


import com.mall.mbg.model.*;
import java.util.List;

/**
 * 秒杀商品
 */
public class FlashProduct extends PmsProduct{
    private SmsFlashBehavior flashBehavior;
    private SmsFlashProductRelation flashProductRelation;
    private List<SmsFlashSkuRelation> flashSkuRelationList;
    private List<PmsSkuStock> skuStockList;

    public List<SmsFlashSkuRelation> getFlashSkuRelationList() {
        return flashSkuRelationList;
    }

    public SmsFlashBehavior getFlashBehavior() {
        return flashBehavior;
    }

    public SmsFlashProductRelation getFlashProductRelation() {
        return flashProductRelation;
    }

    public List<PmsSkuStock> getSkuStockList() {
        return skuStockList;
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

    public void setSkuStockList(List<PmsSkuStock> skuStockList) {
        this.skuStockList = skuStockList;
    }
}
