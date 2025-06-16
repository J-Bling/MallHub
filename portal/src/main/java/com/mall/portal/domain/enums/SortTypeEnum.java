package com.mall.portal.domain.enums;

import com.mall.common.exception.ApiException;

public enum SortTypeEnum {
    ID_DESC(1, "id desc"),
    SALE_DESC(2, "sale desc"),
    PRICE_ASC(3, "price asc"),
    PRICE_DESC(4, "price desc");

    private final Integer code;
    private final String orderByClause;

    SortTypeEnum(Integer code,String orderByClause){
        this.orderByClause=orderByClause;
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public static SortTypeEnum fromCode(int code){
        for (SortTypeEnum typeEnum : values()){
            if (typeEnum.code == code){
                return typeEnum;
            }
        }

        throw new ApiException("错误格式");
    }
}
