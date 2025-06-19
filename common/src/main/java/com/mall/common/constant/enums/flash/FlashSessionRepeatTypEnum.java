package com.mall.common.constant.enums.flash;

public enum FlashSessionRepeatTypEnum {
    REPEAT(false,"重复"),
    DESIGNATED(true,"指定日期");

    private Boolean type;
    private String desc;

    FlashSessionRepeatTypEnum(Boolean type,String desc){
        this.type=type;
        this.desc=desc;
    }

    public String getDesc() {
        return desc;
    }

    public Boolean getType() {
        return type;
    }
}
