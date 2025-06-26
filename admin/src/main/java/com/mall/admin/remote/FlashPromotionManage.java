package com.mall.admin.remote;

import com.mall.common.constant.enums.remote.RemoteMethodEnum;
import com.mall.common.domain.ReSetPromotionModel;
import com.mall.mbg.model.SmsFlashProductRelation;
import com.mall.mbg.model.SmsFlashSkuRelation;

import java.util.Map;

public interface FlashPromotionManage extends Manage{
    // 库存/销量操作
    default void incrementFlashStock(Long relationId, Long skuRelationId, int delta) {
        send(RemoteMethodEnum.FLASH_INCREMENT_STOCK, relationId, skuRelationId, delta);
    }

    default void incrementUserBuyCount(Long userId, Long sessionId, Long productId, int delta) {
        send(RemoteMethodEnum.FLASH_INCREMENT_USER_COUNT, userId, sessionId, productId, delta);
    }

    default void incrementFlashSale(Long sessionId, Long relationId, Long productId, Long skuId, int delta) {
        send(RemoteMethodEnum.FLASH_INCREMENT_SALE, sessionId, relationId, productId, skuId, delta);
    }

    // 缓存管理
    default void cleanFlashSession(Long sessionId) {
        send(RemoteMethodEnum.FLASH_CLEAN_SESSION, sessionId);
    }

    default void cleanFlashPromotion(Long promotionId) {
        send(RemoteMethodEnum.FLASH_CLEAN_PROMOTION, promotionId);
    }

    default void resetFlashPromotion(ReSetPromotionModel model) {
        send(RemoteMethodEnum.FLASH_RESET_PROMOTION, model);
    }

    // 商品关联管理
    default void setFlashProductRelation(Long sessionId, SmsFlashProductRelation relation) {
        send(RemoteMethodEnum.FLASH_SET_PRODUCT_RELATION, sessionId, relation);
    }

    default void setFlashSkuRelation(Long relationId, SmsFlashSkuRelation skuRelation) {
        send(RemoteMethodEnum.FLASH_SET_SKU_RELATION, relationId, skuRelation);
    }

    default void batchSetFlashSkuRelations(Long relationId, Map<String, Object> skuMap) {
        send(RemoteMethodEnum.FLASH_SET_BATCH_SKU_RELATION, relationId, skuMap);
    }

    // 库存设置
    default void setFlashProductStock(Long relationId, Integer count) {
        send(RemoteMethodEnum.FLASH_SET_PRODUCT_STOCK, relationId, count);
    }

    default void setFlashSkuStock(Long relationId, Long skuRelationId, Integer count) {
        send(RemoteMethodEnum.FLASH_SET_SKU_STOCK, relationId, skuRelationId, count);
    }

    default void batchSetFlashSkuStocks(Long relationId, Map<String, String> stockMap) {
        send(RemoteMethodEnum.FLASH_SET_BATCH_SKU_STOCK, relationId, stockMap);
    }

    // 商品管理
    default void removeFlashProduct(Long sessionId, Long productId, Long relationId) {
        send(RemoteMethodEnum.FLASH_REMOVE_PRODUCT, sessionId, productId, relationId);
    }
}
