package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsMemberPrice;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsMemberPriceDao {

    /**
     * 批量指定会员价格
     */
    int batchInsert(List<PmsMemberPrice> memberPriceList);

    /**
     * 指定单个商品会员价格
     */
    @Insert("insert into pms_member_price (product_id, member_level_id, member_price, member_level_name) VALUES " +
            "(#{item.productId}, #{item.memberLevelId}, #{item.memberPrice}, #{item.memberLevelName})")
    void insert(PmsMemberPrice price);

    @Delete("DELETE FROM pms_member_price WHERE product_id = #{productId}")
    void deleteByProductId(Long productId);

    @Select("SELECT * FROM pms_member_price WHERE product_id = #{productId}")
    List<PmsMemberPrice> selectByProductId(Long productId);
}