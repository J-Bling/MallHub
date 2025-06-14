package com.mall.common.constant.enums;

public enum OrderStatusEnum {
    ALL(-1, "全部"),
    UNPAID(0, "待付款"),
    PENDING_SHIPMENT(1, "待发货"),
    SHIPPED(2, "已发货"),
    COMPLETED(3, "已完成"),
    CLOSED(4, "已关闭");

    private final int code;
    private final String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据状态码获取枚举实例
     */
    public static OrderStatusEnum getByCode(int code) {
        for (OrderStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的订单状态码: " + code);
    }

    /**
     * 检查状态码是否有效
     */
    public static boolean isValidCode(int code) {
        for (OrderStatusEnum status : values()) {
            if (status.code == code) {
                return true;
            }
        }
        return false;
    }
}
