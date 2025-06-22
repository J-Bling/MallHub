package com.mall.common.constant.enums.rpc;

public enum ProductRpc {
    DEL_RANK("delRank"),
    ADD_PRODUCT("addProduct"),
    DEL_PRODUCT("deleteProduct"),
    UP_DEL_PRODUCT("upToDelProductCache"),
    UP_DEL_PRODUCT_SUB("upToDelProductSubModelCache"),
    UP_DEL_SKU("upToDelSkuStockCache"),
    UP_DEL_STATS("upToDelStats");

    private String method;

    ProductRpc(String method){
        this.method=method;
    }

    public String getMethod() {
        return method;
    }
}
