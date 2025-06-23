package com.mall.admin.dao.pms;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsAlbumProductRelationDao {
    /**
     * 指定那个商品使用那个相册集
     */
    @Insert("insert into pms_product_album_relation (album_id,product_id) values (#{albumId},${productId})")
    int insert(@Param("productId") Long productId,@Param("albumId") Long albumId);

    /**
     * 根据商品ID删除相册关联
     */
    @Delete("DELETE FROM pms_product_album_relation WHERE product_id = #{productId}")
    void deleteByProductId(Long productId);

    /**
     * 根据商品ID获取相册ID列表
     */
    @Select("SELECT album_id FROM pms_product_album_relation WHERE product_id = #{productId}")
    List<Long> selectAlbumIdsByProductId(Long productId);

    /**
     * 根据相册id获取 商品id列表
     */
    @Select("select product_id from pms_product_album_relation where album_id = #{albumId}")
    List<Long> selectProductIdsByAlbumId(Long albumId);
}