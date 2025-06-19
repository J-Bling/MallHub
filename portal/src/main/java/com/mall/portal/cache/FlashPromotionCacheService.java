package com.mall.portal.cache;


import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.SmsFlashProductRelation;
import com.mall.mbg.model.SmsFlashPromotion;
import com.mall.mbg.model.SmsFlashSkuRelation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlashPromotionCacheService extends Cache {
    /**
     *获取 product
     */
    PmsProduct getProduct(long productId);
    /**
     * 获取单个 promotion
     */
    SmsFlashPromotion flashPromotion(long promotionId);
    /**
     * 获取当前所有promotion
     */
    List<SmsFlashPromotion> flashPromotionList();
    /**
     *获取当轮所有场次
     */
    Set<String> getCurrentSessionIds();
    /**
     * 获取下一轮所有场次
     */
    Set<String> getNextSessionIds();
    /**
     * 判断productId是否在该session里面
     */
    Boolean existsSession(long sessionId,long productId);
    /**
     * 获取 product-session关联
     */
    SmsFlashProductRelation flashProductRelation(long sessionId,long productId);
    /**
     * 按 分数降序获取 SmsFlashProductRelationList
     */
    Set<SmsFlashProductRelation> flashProductRelationList(long sessionId,int offset,int limit);
    /**
     * 获取单个 SmsFlashPromotion
     */
    SmsFlashSkuRelation flashSkuRelation(long productRelationId);
    /**
     * 获取 全部SmsFlashPromotion
     */
    List<SmsFlashPromotion> flashSkuRelationList(long productRelationId);
    /**
     * 获取单个 product 库存缓存
     */
    Integer productStockCount(long productRelationId);
    /**
     * 获取单个 skuStock 库存缓存
     */
    Integer skuStockCount(long productRelationId , long skuRelationId);
    /**
     * 获取 该productRelation sku库存缓存
     */
    Map<Long,Integer> skuStockCountMap(long productRelationId);




    /**
     * 修改库存缓存 flashSkuRelationId==0 时只处理总库存
     */
    void incrementProductStock(long flashProductRelationId,long flashSkuRelationId,int delta);

    /**
     * 修改库存销量缓存
     */
    void incrementProductSale(long productId,long skuId,int delta);

    /**
     * 清空结束场次以及附带信息
     */
    void cleanSession(long sessionId);

    /**
     * 清空结束活动以及附带信息
     */
    void cleanPromotion(long promotionId);



    class CacheKeys{
        //存储promotion的缓存
        public static String PromotionKey = "flash-promotion-key";
        //字段生成
        public static String Field(long id){return ""+id;}
        //存储session的缓存
        public static String SessionHashKey(long promotionId){return "flash-session-hash-key:"+promotionId;}
        //promotion的当轮场次
        public static String CurrentSessionSetKey = "flash-promotion-current-session-set-key:";
        //promotion的下轮场次
        public static String NextSessionSetKey= "flash-promotion-next-session-set-key:";
        //session的productId
        public static String SessionProductSetKey(long sessionId){return "flash-session-product-set-key:"+sessionId;}
        //sms_flash_product_relation 缓存 根据sessionId存储
        public static String ProductRelationZSetKey(long sessionId){return "flash-product-relation-z-set-key:"+sessionId;}
        //库存缓存 根据sms_flash_product_relation.id存储
        public static String SkuStockRelationHashKey(long productRelationId){return "flash-sku-stock-relation-hash-key:"+productRelationId;}
        //获取ms_flash_product_relation.id的商品库存 flash_stock
        public static String ProductFlashStockKey(long productRelationId){return "flash-production-stock-key:"+productRelationId;}
        //获取 sms_flash_sku_relation.id的商品库存 flash_stock
        public static String SkuFlashStockKey(long productRelationId){return "flash-sku-stock-hash-key:"+productRelationId;}
    }
}