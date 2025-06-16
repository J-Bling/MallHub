package com.mall.portal.service;

import com.mall.mbg.model.PmsProduct;
import com.mall.portal.domain.enums.SortTypeEnum;
import com.mall.portal.domain.model.ProductCategory;
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
    List<ProductCategory> categoryTreeList();
    /**
     * 获取前台商品详情
     */
    ProductDetail detail(Long id);
}
