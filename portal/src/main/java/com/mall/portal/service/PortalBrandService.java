package com.mall.portal.service;

import com.mall.common.api.ResponsePage;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsProduct;

import java.util.List;

public interface PortalBrandService {
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
    ResponsePage<PmsProduct> productPage(long brandId,int pageNum,int pageSize);
}
