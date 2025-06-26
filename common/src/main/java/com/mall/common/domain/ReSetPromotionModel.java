package com.mall.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ReSetPromotionModel implements Serializable {
    private List<Long> lastPromotionIds;//结束的活动
    private List<Long> currentPromotionIds;//开始的活动

    private List<Long> toBeEndSessionIds;//要结束的场次
    private List<Long> toStartSessionIds;//要开始的场次
    private List<Long> toNextSessionIds;//下一轮要开始的场次

    private Map<Long,List<Long>> sessionIdToProductIds;//当前场次要上架的商品 id
    private Map<Long,Map<Long,Long>> sessionIdToProductRelationIds;//当前场次要上架的商品关联 id
    private Map<Long,Map<Long,Double>> productRelationIdToScore;// 商品关联id与销量

}
