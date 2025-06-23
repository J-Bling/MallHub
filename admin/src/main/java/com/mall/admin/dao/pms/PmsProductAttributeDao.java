package com.mall.admin.dao.pms;

import com.mall.mbg.model.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PmsProductAttributeDao {

//    pms_product_attribute
    /**
     * 增加一个 属性
     */
    int insertAttribute(PmsProductAttribute attribute);

    /**
     * 更新属性 非空不更
     */
    int updateAttribute(PmsProductAttribute attribute);

    /**
     * 删除一个属性
     */
    int deleteAttribute(Long id);

    /**
     * 查找一个属性
     */
    PmsProductAttribute selectAttributeById(Long id);

    /**
     * 根据 属性类型查找所有属性
     */
    List<PmsProductAttribute> selectAttributeByAttributeCategoryId(Long attributeCategoryId);



    /**
     * 检查商品关联属性
     */
    @Select("select count(*) from pms_product where product_attribute_category_id = #{product_attribute_category_id}")
    Integer countProductByAttributeCategoryId(Long attributeId);

//     pms_product_attribute_category
    /**
     * 增加一个属性类型
     */
    int insertAttributeCategory(PmsProductAttributeCategory category);

    /**
     * 增加属性类型属性数量值
     */
    void incrementAttributeCount(@Param("attributeCategoryId") Long attributeCategoryId,@Param("delta") Integer delta);

    /**
     * 增加属性类型的属性参数数量
     */
    void incrementAttributeParamCount(@Param("attributeCategoryId") Long attributeCategoryId,@Param("delta") Integer delta);

    /**
     * 删除一个属性类型
     */
    int deleteAttributeCategory(Long attributeCategoryId);

    /**
     * 根据属性类型id获取属性类型信息
     */
    PmsProductAttributeCategory getProductAttributeCategory(Long attributeCategoryId);

    /**
     * 获取所有属性类型
     */
    List<PmsProductAttributeCategory> getProductAttributeCategoryAll();


//    pms_product_attribute_value
    /**
     * 增加一个属性值
     */
    int insertProductAttributeValue(PmsProductAttributeValue value);

    /**
     * 批量插入属性值
     */
    int batchInsertProductAttributeValue(List<PmsProductAttributeValue> valueList);

    /**
     * 更新属性值
     */
    void updateProductAttributeValue(@Param("productAttributeValueId") Long productAttributeValueId,@Param("value") String value);

    /**
     * 删除一个属性值
     */
    void deleteProductAttributeValue(Long productAttributeValueId);

    /**
     * 清空一个商品属性的某一个属性所有属性值
     */
    int cleanProductAttributeValue(@Param("productId") Long productId,@Param("attributeId") Long attributeId);

    /**
     * 查找一个属性值
     */
    PmsProductAttributeValue getProductAttributeValue(Long id);

    /**
     * 某个属性的所有属性值
     */
    @Select("select * from pms_product_attribute_value where product_attribute_id = #{product_attribute_id}")
    List<PmsProductAttributeValue> getProductAttributeValueList(Long product_attribute_id);

    /**
     * 查找一个商品某个属性的所有属性值
     */
    List<PmsProductAttributeValue> getProductAttributeValueList(@Param("productId") Long productId,@Param("attributeId") Long attributeId);



    // pms_product_category_attribute_relation
    /**
     * 把商品类型和商品属性建立关联
     */
    int insertProductCategoryAttributeRelation(PmsProductCategoryAttributeRelation categoryAttributeRelation);

    /**
     * 取消某个属性和某个商品类型之间关联
     */
    void deleteProductCategoryAttributeRelation(@Param("productCategoryId") Long productCategoryId , @Param("attributeId") Long attributeId);

    /**
     * 根据商品类型获取绑定的商品属性
     */
    List<PmsProductAttribute> getProductAttributeByProductCategoryId(Long productCategoryId);

    /**
     * 根据商品属性获取绑定的商品类型
     */
    List<PmsProductCategory> getProductCategoryByAttributeId(Long attributeId);
}