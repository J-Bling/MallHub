package com.mall.common.constant.enums;

/**
 * 促销类型枚举
 */
public enum PromotionTypeEnum {
    /**
     * 没有促销，使用原价
     */
    ORIGINAL_PRICE(0, "原价"),

    /**
     * 使用促销价
     */
    PROMOTION_PRICE(1, "促销价"),

    /**
     * 使用会员价
     */
    MEMBER_PRICE(2, "会员价"),

    /**
     * 使用阶梯价格（多级优惠）
     */
    TIERED_PRICE(3, "阶梯价格"),

    /**
     * 使用满减价格（满X减Y）
     */
    FULL_REDUCTION_PRICE(4, "满减价格"),

    /**
     * 限时购（秒杀/闪购）
     */
    FLASH_SALE(5, "限时购");

    private final int code;
    private final String description;

    PromotionTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举实例（安全版）
     * @param code 枚举编码
     * @return 对应的枚举实例，未找到时返回null
     */
    public static PromotionTypeEnum fromCodeSafe(int code) {
        for (PromotionTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据code获取枚举实例（严格版）
     * @param code 枚举编码
     * @return 对应的枚举实例
     * @throws IllegalArgumentException 如果code无效
     */
    public static PromotionTypeEnum fromCodeStrict(int code) {
        PromotionTypeEnum type = fromCodeSafe(code);
        if (type == null) {
            throw new IllegalArgumentException("Invalid PromotionType code: " + code);
        }
        return type;
    }

    /**
     * 检查code是否有效
     * @param code 待验证的code
     * @return 如果有效返回true
     */
    public static boolean isValidCode(int code) {
        return fromCodeSafe(code) != null;
    }
}