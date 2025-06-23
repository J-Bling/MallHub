package com.mall.admin.service;

import com.mall.admin.domain.pms.ProductAttributeDetail;
import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsProductAttributeCategory;
import com.mall.mbg.model.PmsProductAttributeValue;

import java.util.List;

/**
 * 商品属性服务接口
 */
public interface PmsProductAttributeService {
    /**
     * 新增 商品属性类型
     */
    int createAttributeCategory(PmsProductAttributeCategory category);
    /**
     * 更新 商品属性类型 名称
     */
    int updateAttributeCategoryName(PmsProductAttributeCategory category);
    /**
     * 删除一个 商品属性类型
     */
    int deleteAttributeCategory(Long categoryId);
    /**
     * 获取所有商品属性类型
     */
    List<PmsProductAttributeCategory> getProductAttributeCategoryAll();
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
    ProductAttributeDetail getAttributeDetail(Long id);

    /**
     * 获取分类所有属性列表
     */
    List<PmsProductAttribute> listAttributes(Long attributeCategoryId);

    /**
     * 增加一个属性值
     */
    int createAttributeValue(PmsProductAttributeValue value);
    /**
     * 增加多个属性值
     */
    int createAttributeValues(List<PmsProductAttributeValue> values);
    /**
     * 删除属性值
     */
    void deleteAttributeValue(Long attributeValueId);
    /**
     * 修改一个属性值
     */
    void updateAttributeValue(Long attributeValueId,String value);
    /**
     * 清空属性值
     */
    void cleanAttribute(Long productId,Long attributeId);
}