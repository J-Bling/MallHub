package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsBrand;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsBrandDao {

    @Insert("INSERT INTO pms_brand (" +
            "name, first_letter, sort, factory_status, show_status, " +
            "product_count, product_comment_count, logo, big_pic, brand_story" +
            ") VALUES (" +
            "#{name}, #{firstLetter}, #{sort}, #{factoryStatus}, #{showStatus}, " +
            "#{productCount}, #{productCommentCount}, #{logo}, #{bigPic}, #{brandStory}" +
            ")")
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

    // 新增方法：根据名称模糊查询品牌
    @Select("SELECT * FROM pms_brand WHERE name LIKE CONCAT('%', #{name}, '%') ORDER BY sort DESC")
    List<PmsBrand> searchByName(@Param("name") String name);

    // 新增方法：批量更新品牌排序
    @Update("<script>" +
            "UPDATE pms_brand SET sort = CASE id " +
            "<foreach collection='list' item='item' separator=' '>" +
            "WHEN #{item.id} THEN #{item.sort} " +
            "</foreach>" +
            "END WHERE id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item.id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateSort(@Param("list") List<PmsBrand> brands);


    @Select("SELECT * FROM pms_brand " +
            "WHERE (name LIKE CONCAT('%', #{keyword}, '%') OR first_letter = #{firstLetter}) " +
            "AND show_status = #{showStatus} " +
            "ORDER BY sort DESC " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<PmsBrand> selectByPage(@Param("keyword") String keyword,
                                @Param("firstLetter") String firstLetter,
                                @Param("showStatus") Integer showStatus,
                                @Param("offset") Integer offset,
                                @Param("pageSize") Integer pageSize);

    // 新增方法：统计品牌数量
    @Select("SELECT COUNT(*) FROM pms_brand " +
            "WHERE (name LIKE CONCAT('%', #{keyword}, '%') OR first_letter = #{firstLetter}) " +
            "AND show_status = #{showStatus}")
    int countByCondition(@Param("keyword") String keyword,
                         @Param("firstLetter") String firstLetter,
                         @Param("showStatus") Integer showStatus);

    @Update("UPDATE pms_brand SET recommend_status = #{recommendStatus} WHERE id = #{id}")
    int updateRecommendStatus(@Param("id") Long id, @Param("recommendStatus") Integer recommendStatus);
}