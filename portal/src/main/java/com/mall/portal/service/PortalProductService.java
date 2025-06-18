package com.mall.portal.service;

import com.mall.mbg.model.*;
import com.mall.portal.domain.enums.SortTypeEnum;
import com.mall.portal.domain.model.ProductDetail;

import java.util.List;

public interface PortalProductService {
    /**
     * 综合搜索商品
     */
    List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, SortTypeEnum sortTypeEnum);
    /**
     * 以树形结构获取所有商品分类
     */
    List<PmsProductCategory> categoryTreeList();
    /**
     * 获取前台商品详情
     */
    ProductDetail detail(Long id);
    /**
     * 获取商品
     */
    PmsProduct getProduct(long productId);
    /**
     * 获取该商品所有库存信息
     */
    List<PmsSkuStock> getSkuStock(long productId);
    /**
     * 获取该商品所有属性
     */
    List<PmsProductAttribute> getProductAttribute(long productId);
    /**
     * 根据 skuId 获取 sku
     */
    PmsSkuStock getSkuStock(Long skuId,Long productId);
    /**
     * 获取该商品的减满信息
     */
    List<PmsProductFullReduction> getProductFullReductions(long productId);
    /**
     * 获取该商品的价格阶梯
     */
    List<PmsProductLadder> getProductLadders(long productId);
}