package com.mall.common.constant.enums.rpc;

public enum PromotionRpc {
    NEXT_PROMOTION("setNextPromotion"),
    INCREMENT_PRODUCT_STOCK("incrementProductStock"),
    DEL_PRODUCT("delProduct"),
    DEL_SESSION("delSession");

    private String method;
    PromotionRpc(String method){
        this.method=method;
    }

    public String getMethod() {
        return method;
    }
}
