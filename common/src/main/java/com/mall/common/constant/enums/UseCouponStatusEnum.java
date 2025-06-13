package com.mall.common.constant.enums;

import com.mall.common.exception.Assert;

import java.util.Objects;

public enum UseCouponStatusEnum {
    /**
     * 未使用（可正常核销）
     */
    UNUSED(0, "未使用"),

    /**
     * 已使用（已完成核销）
     */
    USED(1, "已使用"),

    /**
     * 已过期（未使用但超过有效期）
     */
    EXPIRED(2, "已过期");

    private Integer code;
    private String description;
    UseCouponStatusEnum(Integer code,String description){
        this.code=code;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCode() {
        return code;
    }

    public static UseCouponStatusEnum formCode(Integer code){
        for (UseCouponStatusEnum useCouponStatusEnum : values()){
            if (Objects.equals(useCouponStatusEnum.code, code)){
                return useCouponStatusEnum;
            }
        }
        Assert.fail("");
        return null;
    }
}
