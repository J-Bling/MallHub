package com.mall.portal.service;

import com.mall.mbg.model.CmsSubject;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductCategory;
import com.mall.portal.domain.model.HomeContent;

import java.util.List;

/**
 * 首页内容服务
 */
public interface HomeService {
    /**
     *获取首页信息
     */
    HomeContent content();
    /**
     * 分页商品推荐
     */
    List<PmsProduct> recommendProductList(int pageNum ,int pageSize);
    /**
     * 获取商品分类 parentId = 0获取第一级
     */
    List<PmsProductCategory> getProductCategoryList(long parentId);
    /**
     * 分页根据分类Id获取专题
     */
    List<CmsSubject> getSubject(long categoryId , int pageNum,int pageSize);
    /**
     * 分页获取人气商品
     */
    List<PmsProduct> getHotProduct(int pageNum,int pageSize);
    /**
     * 分页获取新品商品
     */
    List<PmsProduct> getNewProduct(int pageNum,int pageSize);
}
