package com.mall.portal.service;

import com.mall.mbg.model.SmsFlashBehavior;
import com.mall.portal.domain.model.flash.*;

import java.util.List;

/**
 * 秒杀抢购管理
 */
public interface FlashPromotionService {
    /**
     * 根据 productId 获取商品的秒杀信息
     */
    FlashProductRelation getFlashProductRelation(long productId);
    /**
     * 分页按排行榜热度获取当前场次的秒杀商品
     */
    List<FlashProduct> getFlashProductList(long sessionId,int offset,int limit);
    /**
     *获取已经开始的活动 type =1 平台活动 2 商家活动
     */
    List<FlashPromotion> getStartFlashPromotion(byte type);
    /**
     * 获取7天以内要开始的所有秒杀活动
     */
    List<FlashPromotion> getAllFlashPromotion();
    /**
     * 获取7天内还没有开始的秒杀活动
     */
    List<FlashPromotion> getPreparationFlashPromotion();
    /**
     * 更新 秒杀商品库存 缓存 flashSkuRelationId 库可以为0; 还没有写库
     */
    boolean incrementProductStock(long flashProductRelationId,long flashSkuRelationId,int delta);
    /**
     * 订阅 一个秒杀场次 只会在该场次开始通知
     */
    void subscribeFlashSession(long flashSessionId);
    /**
     * 订阅抢购商品
     */
    void subscribeFlashProduct(long productId,long skuId);
    /**
     * 取消订阅抢购场次
     */
    void unSubscribeFlashSession(long flashSessionId);
    /**
     * 取消订阅商品
     */
    void unSubscribeFlashProduct(long productId,long skuId);
    /**
     * 获取当前用户订阅的历史抢购场次 按时间降序
     */
    List<FlashSubscribeSessionHistory> getSubscribeSessionHistoryList(int offset,int limit);
    /**
     * 获取当前用户订阅的商品 按时间降序
     */
    List<FlashSubscribeProductHistory> getSubscribeProductHistoryList(int offset,int limit);
    /**
     * 获取用户秒杀行为记录
     */
    List<SmsFlashBehavior> getUserBehaviorList(long sessionId, long productId);
    /**
     * 回去用户所有历史抢购行为
     */
    List<FlashBehavior> getFlashBehaviorList(int offset,int limit);
}