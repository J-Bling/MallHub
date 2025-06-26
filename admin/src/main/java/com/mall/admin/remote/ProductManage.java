package com.mall.admin.remote;

import com.mall.common.constant.enums.remote.RemoteMethodEnum;

public interface ProductManage extends Manage{

    // 基础商品操作
    default void addProduct(Long id) {
        send(RemoteMethodEnum.PRODUCT_ADD, id);
    }

    default void deleteProduct(Long id) {
        send(RemoteMethodEnum.PRODUCT_DELETE, id);
    }

    // 缓存管理
    default void deleteProductCache(Long id) {
        send(RemoteMethodEnum.PRODUCT_DELETE_CACHE, id);
    }

    default void deleteProductModelCache(Long productId) {
        send(RemoteMethodEnum.PRODUCT_DELETE_MODEL_CACHE, productId);
    }

    default void deleteSkuStockCache(Long productId) {
        send(RemoteMethodEnum.PRODUCT_DELETE_SKU_STOCK, productId);
    }

    default void deleteSkuStockCountCache(Long skuId) {
        send(RemoteMethodEnum.PRODUCT_DELETE_SKU_STOCK_COUNT, skuId);
    }

    default void deleteProductStatsCache(Long productId) {
        send(RemoteMethodEnum.PRODUCT_DELETE_STATS, productId);
    }

    // 增量操作
    default void incrementProductSale(Long productId, int delta) {
        send(RemoteMethodEnum.PRODUCT_INCREMENT_SALE, productId, delta);
    }

    default void incrementProductStock(Long product, int delta) {
        send(RemoteMethodEnum.PRODUCT_INCREMENT_STOCK, product, delta);
    }

    default void incrementSkuStock(Long productId, Long skuId, int delta) {
        send(RemoteMethodEnum.PRODUCT_INCREMENT_SKU_STOCK, productId, skuId, delta);
    }

    default void incrementSkuSale(Long productId, Long skuId, int delta) {
        send(RemoteMethodEnum.PRODUCT_INCREMENT_SKU_SALE, productId, skuId, delta);
    }

    // 特殊操作
    default void deleteRank() {
        send(RemoteMethodEnum.PRODUCT_DELETE_RANK);
    }

    default void increaseSales(Long id, int sales) {
        send(RemoteMethodEnum.PRODUCT_INCREASE_SALES, id, sales);
    }
}
