package com.mall.portal.domain.model;

import com.mall.mbg.model.CmsSubject;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.SmsHomeAdvertise;
import java.util.List;

public class HomeContent {
    //轮播广告
    private List<SmsHomeAdvertise> advertiseList;
    //推荐品牌
    private List<PmsBrand> brandList;
    //当前秒杀场次
    private FlashPromotion flashPromotion;
    //新品推荐
    private List<PmsProduct> newProductList;
    //人气推荐
    private List<PmsProduct> hotProductList;
    //推荐专题
    private List<CmsSubject> subjectList;

    public FlashPromotion getFlashPromotion() {
        return flashPromotion;
    }

    public List<CmsSubject> getSubjectList() {
        return subjectList;
    }

    public List<PmsBrand> getBrandList() {
        return brandList;
    }

    public List<PmsProduct> getHotProductList() {
        return hotProductList;
    }

    public List<PmsProduct> getNewProductList() {
        return newProductList;
    }

    public List<SmsHomeAdvertise> getAdvertiseList() {
        return advertiseList;
    }

    public void setAdvertiseList(List<SmsHomeAdvertise> advertiseList) {
        this.advertiseList = advertiseList;
    }

    public void setBrandList(List<PmsBrand> brandList) {
        this.brandList = brandList;
    }

    public void setFlashPromotion(FlashPromotion flashPromotion) {
        this.flashPromotion = flashPromotion;
    }

    public void setHotProductList(List<PmsProduct> hotProductList) {
        this.hotProductList = hotProductList;
    }

    public void setNewProductList(List<PmsProduct> newProductList) {
        this.newProductList = newProductList;
    }

    public void setSubjectList(List<CmsSubject> subjectList) {
        this.subjectList = subjectList;
    }
}
