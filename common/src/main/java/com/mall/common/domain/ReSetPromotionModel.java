package com.mall.common.domain;

import com.mall.common.constant.interfaces.ParamsModel;

import java.util.List;
import java.util.Map;

public class ReSetPromotionModel implements ParamsModel {
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
