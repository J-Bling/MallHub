package com.mall.portal.dao;

import com.mall.portal.domain.model.flash.FlashBehavior;
import com.mall.portal.domain.model.flash.FlashSubscribeProductHistory;
import com.mall.portal.domain.model.flash.FlashSubscribeSessionHistory;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromotionDao {
    /**
     * 获取用户秒杀场次订阅历史（带分页）
     */
    List<FlashSubscribeSessionHistory> getFlashSubscribeSessionHistoryList(
            @Param("memberId") long memberId,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 获取用户秒杀商品订阅历史（带分页）
     */
    List<FlashSubscribeProductHistory> getFlashSubscribeProductHistoryList(
            @Param("memberId") long memberId,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 获取用户所有历史抢购 按时间降序 带分页
     */
    List<FlashBehavior> getFlashBehaviorList(
            @Param("memberId") long memberId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
