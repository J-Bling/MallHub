package com.mall.admin.service;

import com.mall.admin.domain.pms.ProductCategoryTreeNodeDTO;
import com.mall.mbg.model.PmsProductCategory;
import java.util.List;

/**
 * 商品分类服务接口
 */
public interface PmsProductCategoryService {

    /**
     * 创建商品分类
     */
    int createCategory(PmsProductCategory category);

    /**
     * 更新商品分类
     */
    int updateCategory(Long id, PmsProductCategory category);

    /**
     * 删除商品分类
     */
    int deleteCategory(Long id);

    /**
     * 获取分类详情
     */
    PmsProductCategory getCategory(Long id);

    /**
     * 获取分类树
     */
    List<ProductCategoryTreeNodeDTO> getCategoryTree();

    /**
     * 更新导航栏显示状态
     */
    int updateNavStatus(Long id, Integer navStatus);

    /**
     * 更新显示状态
     */
    int updateShowStatus(Long id, Integer showStatus);

    /**
     * 获取分类及其子分类ID列表
     */
    List<Long> getCategoryAndChildrenIds(Long id);
}