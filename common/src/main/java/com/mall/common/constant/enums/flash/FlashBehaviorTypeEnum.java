package com.mall.common.constant.enums.flash;

public enum FlashBehaviorTypeEnum {
    SUBSCRIBE(1,"订阅"),
    PARTICIPATE(2,"参与"),
    PLACE_ORDER(3,"下单");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    FlashBehaviorTypeEnum(Integer code,String desc){
        this.code = code;
        this.desc=desc;
    }
}
