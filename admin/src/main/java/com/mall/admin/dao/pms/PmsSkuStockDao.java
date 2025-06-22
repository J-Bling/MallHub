package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsSkuStock;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsSkuStockDao {

    @Insert("<script>" +
            "INSERT INTO pms_sku_stock (" +
            "product_id, sku_code, price, stock, promotion_price, " +
            "low_stock, pic, sale, lock_stock, sp_data" +
            ") VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.skuCode}, #{item.price}, #{item.stock}, #{item.promotionPrice}, " +
            "#{item.lowStock}, #{item.pic}, #{item.sale}, #{item.lockStock}, #{item.spData})" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<PmsSkuStock> skuStockList);

    @Delete("DELETE FROM pms_sku_stock WHERE product_id = #{productId}")
    void deleteByProductId(Long productId);

    @Select("SELECT * FROM pms_sku_stock WHERE product_id = #{productId} ORDER BY id ASC")
    List<PmsSkuStock> selectByProductId(Long productId);

    @Select("SELECT * FROM pms_sku_stock WHERE id = #{id}")
    PmsSkuStock selectById(Long id);

    @Select("SELECT COUNT(*) FROM pms_sku_stock WHERE product_id = #{productId}")
    int countByProductId(Long productId);

//    @Update("UPDATE pms_sku_stock SET " +
//            "lock_stock = lock_stock + #{quantity} " +
//            "WHERE id = #{skuId} AND stock - lock_stock >= #{quantity}")
//    int lockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
    @Update("UPDATE pms_sku_stock SET " +
            "lock_stock = lock_stock + #{quantity} " +
            "WHERE id = #{skuId} AND stock - lock_stock >= #{quantity}")
    int lockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Update("UPDATE pms_sku_stock SET " +
            "stock = stock + #{quantity}, " +
            "lock_stock = lock_stock - #{quantity} " +
            "WHERE id = #{skuId} AND lock_stock >= #{quantity}")
    int releaseStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Update("UPDATE pms_sku_stock SET " +
            "stock = stock - #{quantity} " +
            "WHERE id = #{skuId} AND stock >= #{quantity}")
    int reduceStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Select("SELECT COUNT(DISTINCT sku_code) FROM pms_sku_stock WHERE product_id = #{productId}")
    int countSkuCodeByProductId(Long productId);
}