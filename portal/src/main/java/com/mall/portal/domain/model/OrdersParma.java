package com.mall.portal.domain.model;

import java.io.Serializable;
import java.util.List;

public class OrdersParma implements Serializable {
    //"收货地址ID"
    private Long memberReceiveAddressId;
    //"优惠券ID"
    private Long couponId;
    //"使用的积分数"
    private Integer useIntegration;
    //"支付方式"
    private Integer payType;
    //"被选中的购物车商品ID"
    private List<Long> cartIds;

    public Integer getPayType() {
        return payType;
    }

    public Integer getUseIntegration() {
        return useIntegration;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberReceiveAddressId() {
        return memberReceiveAddressId;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public void setCartIds(List<Long> cartIds) {
        this.cartIds = cartIds;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public void setMemberReceiveAddressId(Long memberReceiveAddressId) {
        this.memberReceiveAddressId = memberReceiveAddressId;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public void setUseIntegration(Integer useIntegration) {
        this.useIntegration = useIntegration;
    }
}
