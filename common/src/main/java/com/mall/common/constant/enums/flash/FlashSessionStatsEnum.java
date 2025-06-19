package com.mall.common.constant.enums.flash;

public enum FlashSessionStatsEnum {
    START(true,"开启"),
    END(false,"关闭");

    private Boolean type;
    private String desc;

    FlashSessionStatsEnum(Boolean type,String desc){
        this.type = type;
        this.desc= desc;
    }

    public Boolean getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
