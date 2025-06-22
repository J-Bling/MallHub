package com.mall.admin.service;

import com.mall.mbg.model.*;

import java.util.List;

/**
 * 商品促销服务接口
 */
public interface PmsProductPromotionService {

    /**
     * 设置商品阶梯价格
     */
    int setProductLadder(Long productId, List<PmsProductLadder> ladderList);

    /**
     * 设置商品满减价格
     */
    int setProductFullReduction(Long productId, List<PmsProductFullReduction> fullReductionList);

    /**
     * 设置商品会员价格
     */
    int setMemberPrice(Long productId, List<PmsMemberPrice> memberPriceList);

    /**
     * 获取当前促销商品列表
     */
    List<PmsProduct> getPromotionProducts();
}