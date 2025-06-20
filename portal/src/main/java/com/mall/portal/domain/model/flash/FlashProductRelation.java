package com.mall.portal.domain.model.flash;

import com.mall.mbg.model.*;

import java.util.List;

/**
 * 秒杀商品关联 该商品关联的秒杀 活动 场次 当前用户的抢购行为 该商品各个sku的库存
 */
public class FlashProductRelation extends SmsFlashProductRelation{
    private SmsFlashPromotion flashPromotion;
    private SmsFlashSession flashSession;
    private Integer buyCount;
    private List<SmsFlashSkuRelation> flashSkuRelationList;

    public Integer getBuyCount() {
        return buyCount;
    }

    public SmsFlashPromotion getFlashPromotion() {
        return flashPromotion;
    }

    public SmsFlashSession getFlashSession() {
        return flashSession;
    }

    public List<SmsFlashSkuRelation> getFlashSkuRelationList() {
        return flashSkuRelationList;
    }

    public void setFlashPromotion(SmsFlashPromotion flashPromotion) {
        this.flashPromotion = flashPromotion;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public void setFlashSession(SmsFlashSession flashSession) {
        this.flashSession = flashSession;
    }

    public void setFlashSkuRelationList(List<SmsFlashSkuRelation> flashSkuRelationList) {
        this.flashSkuRelationList = flashSkuRelationList;
    }
}
