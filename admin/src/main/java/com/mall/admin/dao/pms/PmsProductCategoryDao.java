package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsProductCategoryDao {

    @Insert("INSERT INTO pms_product_category (" +
            "parent_id, name, level, product_count, product_unit, " +
            "nav_status, show_status, sort, icon, keywords, description" +
            ") VALUES (" +
            "#{parentId}, #{name}, #{level}, #{productCount}, #{productUnit}, " +
            "#{navStatus}, #{showStatus}, #{sort}, #{icon}, #{keywords}, #{description}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCategory(PmsProductCategory category);

    @Update("UPDATE pms_product_category SET " +
            "parent_id = #{parentId}, " +
            "name = #{name}, " +
            "level = #{level}, " +
            "product_count = #{productCount}, " +
            "product_unit = #{productUnit}, " +
            "nav_status = #{navStatus}, " +
            "show_status = #{showStatus}, " +
            "sort = #{sort}, " +
            "icon = #{icon}, " +
            "keywords = #{keywords}, " +
            "description = #{description} " +
            "WHERE id = #{id}")
    int updateCategory(PmsProductCategory category);

    @Delete("DELETE FROM pms_product_category WHERE id = #{id}")
    int deleteCategory(Long id);

    @Select("SELECT * FROM pms_product_category WHERE id = #{id}")
    PmsProductCategory selectById(Long id);

    @Select("SELECT * FROM pms_product_category WHERE parent_id = #{parentId} ORDER BY sort DESC")
    List<PmsProductCategory> selectByParentId(Long parentId);

    @Select("SELECT COUNT(*) FROM pms_product_category WHERE name = #{name} AND parent_id = #{parentId}")
    int countByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    @Select("SELECT COUNT(*) FROM pms_product_category WHERE parent_id = #{parentId}")
    int countByParentId(Long parentId);

    @Update("UPDATE pms_product_category SET nav_status = #{navStatus} WHERE id = #{id}")
    int updateNavStatus(@Param("id") Long id, @Param("navStatus") Integer navStatus);

    @Update("UPDATE pms_product_category SET show_status = #{showStatus} WHERE id = #{id}")
    int updateShowStatus(@Param("id") Long id, @Param("showStatus") Integer showStatus);

    @Update("UPDATE pms_product_category SET product_count = product_count + #{delta} WHERE id = #{id}")
    void updateProductCount(@Param("id") Long id, @Param("delta") Integer delta);

    @Select("SELECT COUNT(*) FROM pms_product WHERE product_category_id = #{categoryId} AND delete_status = 0")
    int countProductByCategoryId(Long categoryId);
}