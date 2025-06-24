package com.mall.common.constant.enums.rpc;

public enum CouponRpc {
    DEL_COUPON("delCacheCoupon"),
    DEL_COUPON_PRODUCT_RELATION("delCacheCouponProductRelation"),
    DEL_COUPON_PRO_CATE_RELATION("delCacheCouponProductCateRelation");


    private String method;
    CouponRpc(String method){
        this.method=method;
    }

    public String getMethod() {
        return method;
    }
}
