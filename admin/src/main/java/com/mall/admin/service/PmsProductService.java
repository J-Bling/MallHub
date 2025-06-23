package com.mall.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.admin.domain.pms.ProductParam;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsSkuStock;

import java.util.List;
import java.util.Map;

/**
 * 商品管理
 */
public interface PmsProductService {
    /**
     * 创建商品
     */
    void create(ProductParam productParam) throws JsonProcessingException;
    /**
     * 获取商品信息
     */
    ProductParam getProductParam(Long id);
    /**
     * 更新商品 这里不能修改库存数量
     */
    void updateProduct(Long id, PmsProduct product) throws JsonProcessingException;
    /**
     * 修改商品库存信息 这里不能修改库存数量
     */
    void updateSku(Long productId,List<PmsSkuStock> skuStockList) throws JsonProcessingException;
    /**
     * 修改库存数量 skuMap -> skuId : 数量
     */
    void updateStockCount(Long productId, Map<Long,Integer> skuMap) throws JsonProcessingException;
    /**
     * 批量修改 商品上架状态
     */
    void updateProductPublishStatus(List<Long> ids,Integer publishStatus) throws JsonProcessingException;
    /**
     * 批量修改新品状态 ->要通知前台删除排行榜
     */
    void updateNewStatus(List<Long> ids,Integer newStatus) throws JsonProcessingException;
    /**
     * 批量删除商品
     */
    void updateDeleteStatus(List<Long> ids,Integer deleteStatus) throws JsonProcessingException;
    /**
     * 批量修改商品推荐状态
     */
    void updateRecommendStatus(List<Long> ids,Integer recommendStatus) throws JsonProcessingException;
    /**
     * 根据品牌查询商品
     */
    List<ProductParam> getByBrandId(Long brandId,int offset,int limit);
    /**
     * 根据商品分类查询商品
     */
    List<ProductParam> getByCateId(Long cateId,int offset,int limit);
}