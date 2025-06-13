package com.mall.common.constant.enums;

public enum CouponUseTypeEnum {
    ALL(0, "全场通用"),
    CATEGORY(1, "指定分类"),
    PRODUCT(2, "指定商品"),
    BRAND(3, "指定品牌"),
    STORE(4, "指定店铺"),
    PLATFORM(5, "平台专用"),
    NEW_USER(6, "新用户专享"),
    MEMBER_LEVEL(7, "会员等级专属"),
    ACTIVITY(8, "活动专用"),
    CHANNEL(9, "特定渠道");

    private final Integer code;
    private final String description;

    CouponUseTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举
     */
    public static CouponUseTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponUseTypeEnum scope : values()) {
            if (scope.getCode().equals(code)) {
                return scope;
            }
        }
        return null;
    }

    /**
     * 检查code是否有效
     */
    public static boolean isValid(Integer code) {
        return getByCode(code) != null;
    }
}
