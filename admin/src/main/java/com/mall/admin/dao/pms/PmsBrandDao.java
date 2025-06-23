package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsBrand;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsBrandDao {

    @Insert("INSERT INTO pms_brand (name, first_letter, sort, factory_status, show_status, product_count, product_comment_count, logo, big_pic, brand_story) " +
            "VALUES (#{name}, #{firstLetter}, #{sort}, #{factoryStatus}, #{showStatus}, #{productCount}, #{productCommentCount}, #{logo}, #{bigPic}, #{brandStory})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertBrand(PmsBrand brand);

    @Update("UPDATE pms_brand SET " +
            "name = #{name}, " +
            "first_letter = #{firstLetter}, " +
            "sort = #{sort}, " +
            "factory_status = #{factoryStatus}, " +
            "show_status = #{showStatus}, " +
            "product_count = #{productCount}, " +
            "product_comment_count = #{productCommentCount}, " +
            "logo = #{logo}, " +
            "big_pic = #{bigPic}, " +
            "brand_story = #{brandStory} " +
            "WHERE id = #{id}")
    int updateBrand(PmsBrand brand);

    @Delete("DELETE FROM pms_brand WHERE id = #{id}")
    int deleteBrand(Long id);

    @Select("SELECT * FROM pms_brand WHERE id = #{id}")
    PmsBrand selectById(Long id);

    @Select("SELECT * FROM pms_brand ORDER BY sort DESC")
    List<PmsBrand> selectAll();

    @Update("UPDATE pms_brand SET show_status = #{showStatus} WHERE id = #{id}")
    int updateShowStatus(@Param("id") Long id, @Param("showStatus") Integer showStatus);

    @Update("UPDATE pms_brand SET factory_status = #{factoryStatus} WHERE id = #{id}")
    int updateFactoryStatus(@Param("id") Long id, @Param("factoryStatus") Integer factoryStatus);

    @Select("SELECT COUNT(*) FROM pms_brand WHERE name = #{name}")
    int countByName(String name);

    @Select("SELECT COUNT(*) FROM pms_product WHERE brand_id = #{brandId} AND delete_status = 0")
    int countProductByBrandId(Long brandId);

    @Update("update pms_brand set product_count = product_count + #{count} where id = #{id}")
    int incrementProductCount(@Param("id") Long id,@Param("count") Integer count);

    @Update("update pms_brand set product_comment_count = product_comment_count + #{count} where id = #{id}")
    int incrementProductCommentCount(@Param("id") Long id,@Param("count") Integer count);
}