package com.mall.admin.dao.pms;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsProductAlbumRelationDao {

    /**
     * 批量插入商品相册关联关系
     */
    @Insert("<script>" +
            "INSERT INTO pms_product_album_relation (" +
            "product_id, album_id" +
            ") VALUES " +
            "<foreach collection='albumIds' item='albumId' separator=','>" +
            "(#{productId}, #{albumId})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("productId") Long productId,
                    @Param("albumIds") List<Long> albumIds);

    /**
     * 根据商品ID删除相册关联关系
     */
    @Delete("DELETE FROM pms_product_album_relation WHERE product_id = #{productId}")
    int deleteByProductId(Long productId);

    /**
     * 根据相册ID删除关联关系
     */
    @Delete("DELETE FROM pms_product_album_relation WHERE album_id = #{albumId}")
    int deleteByAlbumId(Long albumId);

    /**
     * 根据商品ID获取相册图片ID列表
     */
    @Select("SELECT album_id FROM pms_product_album_relation WHERE product_id = #{productId}")
    List<Long> selectAlbumPicIdsByProductId(Long productId);

    /**
     * 根据相册ID获取商品ID列表
     */
    @Select("SELECT product_id FROM pms_product_album_relation WHERE album_id = #{albumId}")
    List<Long> selectProductIdsByAlbumId(Long albumId);

    /**
     * 统计商品关联的相册图片数量
     */
    @Select("SELECT COUNT(*) FROM pms_product_album_relation WHERE product_id = #{productId}")
    int countByProductId(Long productId);

    /**
     * 统计相册图片关联的商品数量
     */
    @Select("SELECT COUNT(*) FROM pms_product_album_relation WHERE album_id = #{albumId}")
    int countByAlbumId(Long albumId);
}