package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductFullReduction;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsProductFullReductionDao {

    @Insert("<script>" +
            "INSERT INTO pms_product_full_reduction (product_id, full_price, reduce_price) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.fullPrice}, #{item.reducePrice})" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<PmsProductFullReduction> reductionList);

    @Delete("DELETE FROM pms_product_full_reduction WHERE product_id = #{productId}")
    void deleteByProductId(Long productId);

    @Select("SELECT * FROM pms_product_full_reduction WHERE product_id = #{productId} ORDER BY full_price ASC")
    List<PmsProductFullReduction> selectByProductId(Long productId);
}