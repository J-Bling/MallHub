package com.mall.common.constant.enums.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用远程调用方法枚举
 */
@Getter
@AllArgsConstructor
public enum RemoteMethodEnum {

    // 品牌服务方法
    BRAND_ADD("brandCacheService", "addBrand", new String[]{"brandId"}, "添加品牌"),
    BRAND_ADD_ALL("brandCacheService", "addAll", new String[]{}, "批量添加品牌"),
    BRAND_DELETE("brandCacheService", "delBrand", new String[]{"brandId"}, "删除品牌"),
    BRAND_UPDATE("brandCacheService", "updateBrand", new String[]{"brandId"}, "更新品牌"),
    BRAND_CLEAN("brandCacheService", "cleanBrand", new String[]{}, "清空品牌缓存"),

    /* 商品分类服务方法 */
    CATEGORY_ADD("productCategoryCacheService", "addCategory", new String[]{"id"}, "添加商品分类"),
    CATEGORY_DELETE("productCategoryCacheService", "delCategory", new String[]{"id"}, "删除商品分类"),
    CATEGORY_CLEAN("productCategoryCacheService", "cleanCache", new String[]{}, "清空分类缓存"),

    /* 商品属性服务方法 */
    ATTRIBUTE_ADD_CATE("productAttributeCacheService", "addAttributeCate", new String[]{"cateId"}, "添加属性类型"),
    ATTRIBUTE_ADD("productAttributeCacheService", "addAttribute", new String[]{"attributeId"}, "添加属性"),
    ATTRIBUTE_ADD_ALL("productAttributeCacheService", "addAttributeAll", new String[]{"attributeIds"}, "批量添加属性"),
    ATTRIBUTE_DEL_ALL_BY_CATEGORY("productAttributeCacheService", "delAttributeAllByCategory", new String[]{"categoryId"}, "删除分类下所有属性"),
    ATTRIBUTE_DEL_BY_ID("productAttributeCacheService", "delAttributeById", new String[]{"id", "categoryId"}, "删除指定属性"),
    ATTRIBUTE_DEL_CATEGORY_BY_ID("productAttributeCacheService", "delAttributeCategoryById", new String[]{"id"}, "删除属性分类"),
    ATTRIBUTE_DEL_CATEGORY_ALL("productAttributeCacheService", "delAttributeCategoryAll", new String[]{}, "清空所有属性分类"),

    /* 商品服务-增删改方法 */
    PRODUCT_ADD("productCacheService", "addProduct", new String[]{"id"}, "新增商品"),
    PRODUCT_DELETE("productCacheService", "deleteProduct", new String[]{"id"}, "下架商品"),
    PRODUCT_DELETE_RANK("productCacheService", "deleteRank", new String[]{}, "删除排行榜"),
    PRODUCT_DELETE_CACHE("productCacheService", "delProductCache", new String[]{"id"}, "删除商品缓存"),
    PRODUCT_DELETE_MODEL_CACHE("productCacheService", "delProductModelCache", new String[]{"productId"}, "删除商品模型缓存"),
    PRODUCT_DELETE_SKU_STOCK("productCacheService", "delSkuStock", new String[]{"productId"}, "删除商品SKU库存缓存"),
    PRODUCT_DELETE_SKU_STOCK_COUNT("productCacheService", "delSkuStockCount", new String[]{"skuId"}, "删除SKU库存计数缓存"),
    PRODUCT_DELETE_STATS("productCacheService", "delProductStats", new String[]{"productId"}, "删除商品统计缓存"),
    PRODUCT_INCREMENT_SALE("productCacheService", "incrementProductSale", new String[]{"productId", "delta"}, "增加商品销量"),
    PRODUCT_INCREMENT_STOCK("productCacheService", "incrementProductStock", new String[]{"product", "delta"}, "调整商品总库存"),
    PRODUCT_INCREMENT_SKU_STOCK("productCacheService", "incrementSkuStock", new String[]{"productId", "skuId", "delta"}, "调整SKU库存"),
    PRODUCT_INCREMENT_SKU_SALE("productCacheService", "incrementSkuSale", new String[]{"productId", "skuId", "delta"}, "调整SKU销量"),
    PRODUCT_INCREASE_SALES("productCacheService", "increaseSales", new String[]{"id", "sales"}, "增加排行榜销售额"),

    /* 秒杀活动服务-增删改方法 */
    FLASH_INCREMENT_STOCK("flashPromotionCacheService", "incrementProductStock",
            new String[]{"flashProductRelationId", "flashSkuRelationId", "delta"},
            "修改秒杀库存（0表示只改总库存）"),

    FLASH_INCREMENT_USER_COUNT("flashPromotionCacheService", "incrementUserBuyCount",
            new String[]{"userId", "sessionId", "productId", "delta"},
            "修改用户购买次数"),

    FLASH_INCREMENT_SALE("flashPromotionCacheService", "incrementProductSale",
            new String[]{"sessionId", "productRelationId", "productId", "skuId", "delta"},
            "修改秒杀商品销量"),

    FLASH_CLEAN_SESSION("flashPromotionCacheService", "cleanSession",
            new String[]{"sessionId"},
            "清空结束场次缓存"),

    FLASH_CLEAN_PROMOTION("flashPromotionCacheService", "cleanPromotion",
            new String[]{"promotionId"},
            "删除活动缓存"),

    FLASH_RESET_PROMOTION("flashPromotionCacheService", "reSetPromotion",
            new String[]{"promotionModel"},
            "重置下一轮活动缓存"),

    FLASH_SET_PRODUCT_RELATION("flashPromotionCacheService", "setProductRelationCache",
            new String[]{"sessionId", "productRelation"},
            "设置商品场次关联缓存"),

    FLASH_SET_SKU_RELATION("flashPromotionCacheService", "setSkuRelationCache",
            new String[]{"productRelationId", "skuRelation"},
            "设置单个SKU关联缓存"),

    FLASH_SET_BATCH_SKU_RELATION("flashPromotionCacheService", "setSkuRelationCache",
            new String[]{"productRelationId", "skuMap"},
            "批量设置SKU关联缓存"),

    FLASH_SET_PRODUCT_STOCK("flashPromotionCacheService", "setProductRelationStockCache",
            new String[]{"productRelationId", "count"},
            "设置商品总库存缓存"),

    FLASH_SET_SKU_STOCK("flashPromotionCacheService", "setSkuRelationStockCache",
            new String[]{"productRelationId", "skuRelationId", "count"},
            "设置单个SKU库存缓存"),

    FLASH_SET_BATCH_SKU_STOCK("flashPromotionCacheService", "setSkuRelationStockCache",
            new String[]{"productRelationId", "skuStockMap"},
            "批量设置SKU库存缓存"),

    FLASH_REMOVE_PRODUCT("flashPromotionCacheService", "removedProduct",
            new String[]{"sessionId", "productId", "productRelationId"},
            "下架秒杀商品");

    private final String beanName;      // Spring 容器中的 Bean 名称
    private final String methodName;    // 方法名称
    private final String[] paramNames;  // 参数名称（按顺序）
    private final String description;   // 方法描述
}
