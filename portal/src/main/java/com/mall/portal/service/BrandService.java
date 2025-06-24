package com.mall.portal.service;

import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsProduct;

import java.util.List;

public interface BrandService {
    /**
     * 分页获取品牌列表
     */
    List<PmsBrand> brandList(int pageNum,int pageSize);
    /**
     * 获取品牌详情
     */
    PmsBrand detail(long brandId);
    /**
     * 分页获取品牌相关商品
     */
    List<PmsProduct> productPage(long brandId,int offset,int limit);
    /**
     * 推荐品牌
     */
    List<PmsBrand> recommendBrands(int limit);
}
