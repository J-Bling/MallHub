package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsMemberPrice;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsMemberPriceDao {

    @Insert("<script>" +
            "INSERT INTO pms_member_price (product_id, member_level_id, member_price, member_level_name) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.memberLevelId}, #{item.memberPrice}, #{item.memberLevelName})" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<PmsMemberPrice> memberPriceList);

    @Delete("DELETE FROM pms_member_price WHERE product_id = #{productId}")
    int deleteByProductId(Long productId);

    @Select("SELECT * FROM pms_member_price WHERE product_id = #{productId} ORDER BY member_level_id ASC")
    List<PmsMemberPrice> selectByProductId(Long productId);
}