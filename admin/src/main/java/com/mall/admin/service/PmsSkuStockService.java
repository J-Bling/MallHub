package com.mall.admin.service;

import com.mall.mbg.model.PmsSkuStock;
import java.util.List;

/**
 * 商品SKU库存服务接口
 */
public interface PmsSkuStockService {

    /**
     * 批量更新SKU库存信息
     */
    int updateSkuStock(Long productId, List<PmsSkuStock> skuStockList);

    /**
     * 获取商品SKU列表
     */
    List<PmsSkuStock> getSkuList(Long productId);

    /**
     * 锁定库存
     */
    int lockStock(Long skuId, Integer quantity);

    /**
     * 释放库存
     */
    int releaseStock(Long skuId, Integer quantity);
}