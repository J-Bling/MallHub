package com.mall.portal.service;

import com.mall.mbg.model.OmsCartItem;
import com.mall.portal.domain.model.PromotionCartItem;

import java.util.List;

/**
 * 促销管理
 */
public interface PromotionService {
    /**
     * 计算购物车中的促销活动信息
     */
    List<PromotionCartItem> calcCartPromotion(List<OmsCartItem> cartItemList);
    /**
     * 获取商品促销信息
     */
}
