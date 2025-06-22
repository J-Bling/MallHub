package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductAttributeValue;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsProductAttributeValueDao {

    /**
     * 批量插入商品属性值
     */
    @Insert("<script>" +
            "INSERT INTO pms_product_attribute_value (" +
            "product_id, product_attribute_id, value" +
            ") VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.productAttributeId}, #{item.value})" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<PmsProductAttributeValue> attributeValueList);

    /**
     * 根据商品ID删除属性值
     */
    @Delete("DELETE FROM pms_product_attribute_value WHERE product_id = #{productId}")
    int deleteByProductId(Long productId);

    /**
     * 根据商品ID获取属性值列表
     */
    @Select("SELECT * FROM pms_product_attribute_value WHERE product_id = #{productId}")
    List<PmsProductAttributeValue> selectByProductId(Long productId);

    /**
     * 根据商品ID和属性ID获取属性值
     */
    @Select("SELECT * FROM pms_product_attribute_value " +
            "WHERE product_id = #{productId} AND product_attribute_id = #{attributeId}")
    PmsProductAttributeValue selectByProductAndAttribute(
            @Param("productId") Long productId,
            @Param("attributeId") Long attributeId);

    /**
     * 根据属性ID统计关联商品数量
     */
    @Select("SELECT COUNT(*) FROM pms_product_attribute_value " +
            "WHERE product_attribute_id = #{attributeId}")
    int countByAttributeId(Long attributeId);

    /**
     * 更新商品属性值
     */
    @Update("UPDATE pms_product_attribute_value SET " +
            "value = #{value} " +
            "WHERE id = #{id}")
    int update(PmsProductAttributeValue attributeValue);
}