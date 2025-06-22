package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductLadder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsProductLadderDao {

    @Insert("<script>" +
            "INSERT INTO pms_product_ladder (product_id, count, discount, price) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.count}, #{item.discount}, #{item.price})" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<PmsProductLadder> ladderList);

    @Delete("DELETE FROM pms_product_ladder WHERE product_id = #{productId}")
    int deleteByProductId(Long productId);

    @Select("SELECT * FROM pms_product_ladder WHERE product_id = #{productId} ORDER BY count ASC")
    List<PmsProductLadder> selectByProductId(Long productId);
}