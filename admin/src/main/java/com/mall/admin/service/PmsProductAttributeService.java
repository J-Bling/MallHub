package com.mall.admin.service;

import com.mall.admin.domain.pms.CategoryAttributeRelationDTO;
import com.mall.mbg.model.PmsProductAttribute;
import java.util.List;

/**
 * 商品属性服务接口
 */
public interface PmsProductAttributeService {

    /**
     * 创建商品属性
     */
    int createAttribute(PmsProductAttribute attribute);

    /**
     * 更新商品属性
     */
    int updateAttribute(Long id, PmsProductAttribute attribute);

    /**
     * 删除商品属性
     */
    int deleteAttribute(Long id);

    /**
     * 获取属性详情
     */
    PmsProductAttribute getAttribute(Long id);

    /**
     * 获取属性列表
     */
    List<PmsProductAttribute> listAttributes(Long attributeCategoryId);

    /**
     * 获取分类关联的属性信息
     */
    CategoryAttributeRelationDTO getCategoryAttributeRelation(Long categoryId);
}