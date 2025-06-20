package com.mall.portal.domain.model.flash;


import com.mall.mbg.model.*;
import java.util.List;

/**
 * 秒杀商品
 */
public class FlashProduct extends PmsProduct{
//    private List<SmsFlashBehavior> behaviorList;
    private Integer buyCount;
    private SmsFlashProductRelation flashProductRelation;
    private List<SmsFlashSkuRelation> flashSkuRelationList;
//    private List<PmsSkuStock> skuStockList;

    public Integer getBuyCount() {
        return buyCount;
    }

    public List<SmsFlashSkuRelation> getFlashSkuRelationList() {
        return flashSkuRelationList;
    }

//    public List<SmsFlashBehavior> getBehaviorList() {
//        return behaviorList;
//    }

    public SmsFlashProductRelation getFlashProductRelation() {
        return flashProductRelation;
    }

//    public List<PmsSkuStock> getSkuStockList() {
//        return skuStockList;
//    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public void setFlashSkuRelationList(List<SmsFlashSkuRelation> flashSkuRelationList) {
        this.flashSkuRelationList = flashSkuRelationList;
    }

//    public void setBehaviorList(List<SmsFlashBehavior> behaviorList) {
//        this.behaviorList = behaviorList;
//    }

    public void setFlashProductRelation(SmsFlashProductRelation flashProductRelation) {
        this.flashProductRelation = flashProductRelation;
    }

//    public void setSkuStockList(List<PmsSkuStock> skuStockList) {
//        this.skuStockList = skuStockList;
//    }
}
