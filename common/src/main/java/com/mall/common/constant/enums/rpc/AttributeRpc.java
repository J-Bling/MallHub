package com.mall.common.constant.enums.rpc;

public enum AttributeRpc {
    DEL_ATTRIBUTE_ALL_BY_CATEGORY("delAttributeAllByCategory"),
    DEL_ATTRIBUTE("delAttributeById"),
    DEL_ATTRIBUTE_CATEGORY_BY_ID("delAttributeCategoryById"),
    DEL_ATTRIBUTE_CATEGORY_ALL("delAttributeCategoryAll");

    private final String method;

    AttributeRpc(String method){
        this.method=method;
    }

    public String getMethod() {
        return method;
    }
}
