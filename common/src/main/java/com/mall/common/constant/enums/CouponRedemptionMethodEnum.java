package com.mall.common.constant.enums;

public enum CouponRedemptionMethodEnum {
    /**
     * 主动领取
     */
    ONLINE_CLAIM(1, "线上领取"),
    /**
     * 系统自动发放（如新用户注册赠券）
     */
    AUTO_ISSUE(2, "自动发放"),
    /**
     * 通过活动页面兑换
     */
    CODE_REDEMPTION(3, "兑换码兑换"),

    /**
     * 线下门店领取
     */
    IN_STORE_PICKUP(4, "门店自提");

    private final Integer code; // 英文标识（用于数据库存储或API传输）
    private final String description;

    CouponRedemptionMethodEnum(Integer code,String description){
        this.code=code;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCode() {
        return code;
    }
}
