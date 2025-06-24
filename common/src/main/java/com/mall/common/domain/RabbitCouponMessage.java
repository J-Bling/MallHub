package com.mall.common.domain;

import lombok.Builder;

@Builder
public class RabbitCouponMessage  extends RabbitRpcMessage{
    private Long couponId;
    private Boolean isAllUseType;
    private Long productId;
    private Long productCateId;


    public Long getCouponId() {
        return couponId;
    }

    public Long getProductCateId() {
        return productCateId;
    }

    public Boolean getAllUseType() {
        return isAllUseType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductCateId(Long productCateId) {
        this.productCateId = productCateId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public void setAllUseType(Boolean allUseType) {
        isAllUseType = allUseType;
    }
}
