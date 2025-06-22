package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductAttribute;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsProductAttributeDao {

    @Insert("INSERT INTO pms_product_attribute (" +
            "product_attribute_category_id, name, select_type, input_type, input_list, " +
            "sort, filter_type, search_type, related_status, hand_add_status, type" +
            ") VALUES (" +
            "#{productAttributeCategoryId}, #{name}, #{selectType}, #{inputType}, #{inputList}, " +
            "#{sort}, #{filterType}, #{searchType}, #{relatedStatus}, #{handAddStatus}, #{type}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertAttribute(PmsProductAttribute attribute);

    @Update("UPDATE pms_product_attribute SET " +
            "product_attribute_category_id = #{productAttributeCategoryId}, " +
            "name = #{name}, " +
            "select_type = #{selectType}, " +
            "input_type = #{inputType}, " +
            "input_list = #{inputList}, " +
            "sort = #{sort}, " +
            "filter_type = #{filterType}, " +
            "search_type = #{searchType}, " +
            "related_status = #{relatedStatus}, " +
            "hand_add_status = #{handAddStatus}, " +
            "type = #{type} " +
            "WHERE id = #{id}")
    int updateAttribute(PmsProductAttribute attribute);

    @Delete("DELETE FROM pms_product_attribute WHERE id = #{id}")
    int deleteAttribute(Long id);

    @Select("SELECT * FROM pms_product_attribute WHERE id = #{id}")
    PmsProductAttribute selectById(Long id);

    @Select("SELECT * FROM pms_product_attribute WHERE product_attribute_category_id = #{categoryId} ORDER BY sort DESC")
    List<PmsProductAttribute> selectByCategoryId(Long categoryId);

    @Select("SELECT pa.* FROM pms_product_attribute pa " +
            "JOIN pms_product_category_attribute_relation pcar ON pa.id = pcar.product_attribute_id " +
            "WHERE pcar.product_category_id = #{categoryId} ORDER BY pa.sort DESC")
    List<PmsProductAttribute> selectByProductCategoryId(Long categoryId);

    @Select("SELECT * FROM pms_product_attribute " +
            "WHERE product_attribute_category_id = #{attributeCategoryId} " +
            "AND id NOT IN (" +
            "SELECT product_attribute_id FROM pms_product_category_attribute_relation " +
            "WHERE product_category_id = #{categoryId}" +
            ") ORDER BY sort DESC")
    List<PmsProductAttribute> selectOptionalByCategoryId(@Param("categoryId") Long categoryId,
                                                         @Param("attributeCategoryId") Long attributeCategoryId);

    @Select("SELECT COUNT(*) FROM pms_product_attribute " +
            "WHERE name = #{name} AND product_attribute_category_id = #{categoryId}")
    int countByNameAndCategoryId(@Param("name") String name,
                                 @Param("categoryId") Long categoryId);

    @Select("SELECT COUNT(*) FROM pms_product_attribute_value WHERE product_attribute_id = #{attributeId}")
    int countProductByAttributeId(Long attributeId);
}