package com.mall.common.constant.enums.flash;

public enum FlashPromotionStatusEnum {
    PREPARATION((byte) 0,"准备中"),
    START((byte) 1,"开始"),
    END((byte) 2,"结束"),
    CANCELED((byte) 3 ,"已取消");

    private Byte code;
    private String desc;

    FlashPromotionStatusEnum(Byte code,String desc){
        this.code = code;
        this.desc=desc;
    }

    public String getDesc() {
        return desc;
    }

    public Byte getCode() {
        return code;
    }
}
