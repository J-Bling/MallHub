package com.mall.common.enums;


public enum BusinessErrorCode {
    INVALID_PARAMETER(1001, "参数错误"),
    BRAND_NOT_FOUND(2001, "品牌不存在"),
    BRAND_NAME_EXISTED(2002, "品牌名称已存在"),
    BRAND_HAS_PRODUCTS(2003, "品牌下有关联商品，不能删除"),
    ALBUM_NOT_FOUND(3001, "相册不存在"),
    ALBUM_NAME_EXISTED(3002, "相册名称已存在"),
    ALBUM_NOT_EMPTY(3003, "相册非空，无法删除"),
    ATTRIBUTE_NOT_FOUND(4001, "商品属性不存在"),
    ATTRIBUTE_NAME_EXISTED(4002, "属性名称已存在"),
    ATTRIBUTE_CATEGORY_NOT_FOUND(4003, "属性分类不存在"),
    ATTRIBUTE_HAS_PRODUCTS(4004, "属性有关联商品，不能删除"),
    CATEGORY_NOT_FOUND(5001, "商品分类不存在"),
    CATEGORY_NAME_EXISTED(5002, "分类名称已存在"),
    CATEGORY_HAS_CHILDREN(5003, "分类下有子分类，不能删除"),
    CATEGORY_HAS_PRODUCTS(5004, "分类下有关联商品，不能删除"),
    CATEGORY_PARENT_NOT_FOUND(5005, "父分类不存在"),
    CATEGORY_PARENT_INVALID(5006, "不能设置自己为父分类"),
    COMMENT_NOT_FOUND(6001, "评论不存在"),
    COMMENT_REPLY_INVALID(6002, "回复内容无效"),
    COMMENT_ALREADY_REPLIED(6003, "该评论已回复"),
    PROMOTION_INVALID(7001, "促销信息无效"),
    PROMOTION_TIME_INVALID(7002, "促销时间设置无效"),
    PROMOTION_PRICE_INVALID(7003, "促销价格设置无效"),
    PRODUCT_NOT_FOUND(8001, "商品不存在"),
    PRODUCT_NAME_EXISTED(8002, "商品名称已存在"),
    PRODUCT_SN_EXISTED(8003, "商品货号已存在"),
    PRODUCT_CATEGORY_NOT_FOUND(8004, "商品分类不存在"),
    PRODUCT_CREATE_FAILED(8005, "商品创建失败"),
    PRODUCT_UPDATE_FAILED(8006, "商品更新失败"),
    PRODUCT_DELETE_FAILED(8007, "商品删除失败"),
    SKU_NOT_FOUND(9001, "SKU不存在"),
    SKU_STOCK_NOT_ENOUGH(9002, "SKU库存不足"),
    SKU_LOCK_STOCK_NOT_ENOUGH(9003, "SKU锁定库存不足"),
    SKU_CODE_DUPLICATE(9004, "SKU编码重复");

    private final int code;
    private final String description;

    BusinessErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}