package com.mall.portal.domain.model.flash;

import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponHistory;
import com.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.mall.mbg.model.SmsCouponProductRelation;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class CouponHistoryDetail extends SmsCouponHistory {
    @ApiModelProperty("优惠券信息")
    private SmsCoupon coupon;
    @ApiModelProperty("优惠券关联商品")
    private List<SmsCouponProductRelation> productRelationList;
    @ApiModelProperty("优惠券关联商品分类")
    private List<SmsCouponProductCategoryRelation> categoryRelationList;

    public SmsCoupon getCoupon() {
        return coupon;
    }

    public List<SmsCouponProductRelation> getProductRelationList() {
        return productRelationList;
    }

    public List<SmsCouponProductCategoryRelation> getCategoryRelationList() {
        return categoryRelationList;
    }

    public void setCoupon(SmsCoupon coupon) {
        this.coupon = coupon;
    }

    public void setProductRelationList(List<SmsCouponProductRelation> productRelations) {
        this.productRelationList = productRelations;
    }

    public void setCategoryRelationList(List<SmsCouponProductCategoryRelation> categoryRelations) {
        this.categoryRelationList = categoryRelations;
    }
}
