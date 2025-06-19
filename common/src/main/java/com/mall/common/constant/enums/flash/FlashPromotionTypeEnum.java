package com.mall.common.constant.enums.flash;

public enum FlashPromotionTypeEnum {
    PLATFORM_ACTIVITY((byte)1,"平台活动"),
    SELLER_ACTIVITY((byte)2,"商家活动");

    private Byte code;
    private String desc;

    FlashPromotionTypeEnum(Byte code,String desc){
        this.code = code;
        this.desc=desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
