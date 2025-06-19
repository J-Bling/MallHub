package com.mall.common.constant.enums.flash;

public enum FlashStockFlowOperateType {
    WITHHOLDING(1,"预扣减"),
    CONFIRM(2,"确认"),
    FALLBACK(3,"回退"),
    MANUAL_ADJUSTMENTS(4,"人工调整");

    private Integer code;
    private String desc;

    FlashStockFlowOperateType(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }
}
