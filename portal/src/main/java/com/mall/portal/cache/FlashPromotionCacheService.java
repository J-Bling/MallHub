package com.mall.portal.cache;


import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlashPromotionCacheService extends Cache {
    /**
     *获取 product
     */
    PmsProduct getProduct(long productId);
    /**
     * 获取用户购买情况
     */
    String getUserBuyCount(long userId,long sessionId,long productId);
    /**
     * 获取单个 promotion
     */
    SmsFlashPromotion flashPromotion(long promotionId);
    /**
     * 获取当前所有promotion
     */
    List<SmsFlashPromotion> flashPromotionList();
    /**
     *获取当轮所有场次ids
     */
    Set<String> getCurrentSessionIds();
    /**
     * 获取下一轮所有场次ids
     */
    Set<String> getNextSessionIds();
    /**
     * 判断productId是否在该session里面
     */
    Boolean containProduct(long sessionId,long productId);
    /**
     * 使用productId获取对应的productRelationId
     */
    String getProductRelationId(long sessionId,long productId);
    /**
     * 获取一个 session
     */
    SmsFlashSession getSession(long promotionId,long sessionId);
    /**
     * 根据 promotionId 获取所有 session
     */
    List<SmsFlashSession> getSessions(long promotionId);
    /**
     *分数降序获取  分页获取 productRelationIds
     */
    Set<String> getProductRelationIds(long sessionId,int offset,int limit);
    /**
     * 获取 product-session关联
     */
    SmsFlashProductRelation flashProductRelation(long sessionId,long productRelationId);
    /**
     * 按 ids获取  SmsFlashProductRelationList
     */
    List<SmsFlashProductRelation> flashProductRelationList(long sessionId,Set<String> strings);
    /**
     * 获取单个 SmsFlashPromotion
     */
    SmsFlashSkuRelation flashSkuRelation(long productRelationId,long skuRelationId);
    /**
     * 获取 全部SmsFlashPromotion
     */
    List<SmsFlashSkuRelation> flashSkuRelationList(long productRelationId);
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
    Long incrementProductStock(long flashProductRelationId,long flashSkuRelationId,int delta);
    /**
     * 修改用户购买次数
     */
    void incrementUserBuyCount(long userId,long sessionId,long productId,int delta);
    /**
     * 修改库存销量缓存
     */
    void incrementProductSale(long sessionId,long productRelationId,long productId,long skuId,int delta);
    /**
     * 清空结束场次以及附带信息
     */
    void cleanSession(long sessionId);
    /**
     * 删除该活动
     */
    void cleanPromotion(long promotionId);
    /**
     * 重置下一轮活动
     */
    void reSetPromotion(ReSetPromotionModel promotionModel);



    /**
     *设置 productRelation缓存
     */
    void setProductRelationCache(long sessionId,SmsFlashProductRelation productRelation);
    /**
     * 设置 skuRelation缓存
     */
    void setSkuRelationCache(long productRelationId,SmsFlashSkuRelation skuRelation);
    /**
     * 设置多个skuRelation缓存
     */
    void setSkuRelationCache(long productRelationId,Map<String,Object> skuMap);
    /**
     * 设置 productRelation库存信息
     */
    void setProductRelationStockCache(long productRelationId,int count);
    /**
     * 设置 skuRelation库存信息
     */
    void setSkuRelationStockCache(long productRelationId,long skuRelationId, int count);
    /**
     * 设置多个 skuRelation 库存信息
     */
    void setSkuRelationStockCache(long productRelationId,Map<String,String> skuStockMap);
    /**
     * 下架一个商品
     */
    void removedProduct(long sessionId,long productId,long productRelationId);
    /**
     * 获取锁
     */
    Boolean tryLock(String key,long expire);
    Boolean tryLock(String key);
    /**
     * 解锁
     */
    void unLock(String key);




    class CacheKeys{
        //存储promotion的缓存
        public static String PromotionKey = "flash-promotion-hash-key";
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
        // product-productRelation对应id
        public static String ProductIdToProductRelationId(long sessionId){return "flash-product-to-productRelation-hash-key:"+sessionId;}
        //sms_flash_product_relation.id 排行 缓存 根据sessionId存储
        public static String ProductRelationZSetKey(long sessionId){return "flash-product-relation-z-set-key:"+sessionId;}
        //sms_flash_product_relation 缓存 根据sessionId存储
        public static String ProductRelationHashKey(long sessionId){return "flash-product-relation-hash-key:"+sessionId;}
        //库存缓存 根据sms_flash_product_relation.id存储
        public static String SkuStockRelationHashKey(long productRelationId){return "flash-sku-stock-relation-hash-key:"+productRelationId;}
        //获取ms_flash_product_relation.id的商品库存 flash_stock
        public static String ProductFlashStockKey(long productRelationId){return "flash-production-stock-key:"+productRelationId;}
        //获取 sms_flash_sku_relation.id的商品库存 flash_stock
        public static String SkuFlashStockHashKey(long productRelationId){return "flash-sku-stock-hash-key:"+productRelationId;}
        //用户购买情况
        public static String UserBuyCount(long userId,long sessionId,long productId){return "flash-user-but-count:user:"+userId+"sessionId:"+sessionId+"productId:"+productId;}
    }

    class ReSetPromotionModel implements Serializable {
        private List<Long> lastPromotionIds;//结束的活动
        private List<Long> currentPromotionIds;//开始的活动

        private List<Long> toBeEndSessionIds;//要结束的场次
        private List<Long> toStartSessionIds;//要开始的场次
        private List<Long> toNextSessionIds;//下一轮要开始的场次

        private Map<Long,List<Long>> sessionIdToProductIds;//当前场次要上架的商品 id
        private Map<Long,Map<Long,Long>> sessionIdToProductRelationIds;//当前场次要上架的商品关联 id
        private Map<Long,Map<Long,Double>> productRelationIdToScore;// 商品关联id与销量

        public List<Long> getCurrentPromotionIds() {
            return currentPromotionIds;
        }

        public List<Long> getLastPromotionIds() {
            return lastPromotionIds;
        }

        public List<Long> getToBeEndSessionIds() {
            return toBeEndSessionIds;
        }

        public List<Long> getToNextSessionIds() {
            return toNextSessionIds;
        }

        public List<Long> getToStartSessionIds() {
            return toStartSessionIds;
        }

        public Map<Long, Map<Long, Double>> getProductRelationIdToScore() {
            return productRelationIdToScore;
        }

        public Map<Long, List<Long>> getSessionIdToProductIds() {
            return sessionIdToProductIds;
        }

        public Map<Long, Map<Long, Long>> getSessionIdToProductRelationIds() {
            return sessionIdToProductRelationIds;
        }

        public void setCurrentPromotionIds(List<Long> currentPromotionIds) {
            this.currentPromotionIds = currentPromotionIds;
        }

        public void setLastPromotionIds(List<Long> lastPromotionIds) {
            this.lastPromotionIds = lastPromotionIds;
        }

        public void setProductRelationIdToScore(Map<Long, Map<Long, Double>> productRelationIdToScore) {
            this.productRelationIdToScore = productRelationIdToScore;
        }

        public void setSessionIdToProductIds(Map<Long, List<Long>> sessionIdToProductIds) {
            this.sessionIdToProductIds = sessionIdToProductIds;
        }

        public void setSessionIdToProductRelationIds(Map<Long, Map<Long, Long>> sessionIdToProductRelationIds) {
            this.sessionIdToProductRelationIds = sessionIdToProductRelationIds;
        }

        public void setToBeEndSessionIds(List<Long> toBeEndSessionIds) {
            this.toBeEndSessionIds = toBeEndSessionIds;
        }

        public void setToNextSessionIds(List<Long> toNextSessionIds) {
            this.toNextSessionIds = toNextSessionIds;
        }

        public void setToStartSessionIds(List<Long> toStartSessionIds) {
            this.toStartSessionIds = toStartSessionIds;
        }
    }
}