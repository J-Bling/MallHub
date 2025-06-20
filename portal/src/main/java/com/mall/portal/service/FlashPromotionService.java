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
     * 获取 7天以内 的所有秒杀活动
     */
    List<FlashPromotion> getAllFlashPromotion();
    /**
     * 获取 7 天内 还没有开始的秒杀活动
     */
    List<FlashPromotion> getPreparationFlashPromotion();
    /**
     * 更新 秒杀商品库存 缓存 flashSkuRelationId 库为0
     */
    boolean incrementProductStock(long flashProductRelationId,long flashSkuRelationId,int delta);
    /**
     * 订阅 一个秒杀场次 只会在该场次开始通知
     */
    boolean subscribeFlashSession(long flashSessionId);
    /**
     * 订阅抢购商品
     */
    boolean subscribeFlashProduct(long productId);
    /**
     * 取消订阅抢购场次
     */
    boolean unSubscribeFlashSession(long flashSessionId);
    /**
     * 取消订阅商品
     */
    boolean unSubscribeFlashProduct(long productId);
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
}