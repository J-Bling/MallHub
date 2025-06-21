package com.mall.portal.dao;

import com.mall.mbg.model.PmsProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductDao {
    /**
     * 根据品牌获取相关商品
     */
    @Select("select * from pms_product where brand_id = #{brandId} order id asc limit #{offset},#{limit}")
    List<PmsProduct> getProductByBrandId(@Param("brandId") long brandId,@Param("offset") int offset,@Param("limit") int limit);
    /**
     * 根据销量降序分页获取 product
     */
    @Select("select * from pms_product order by sale desc limit #{offset},#{limit}")
    List<PmsProduct> getProductOfSale(@Param("offset") int offset,@Param("limit") int limit);
    /**
     * 根据新品排行获取前 n个 product
     */
    @Select("select id,create_at from pms_product where new_status = 1 and verify_status =1 and delete_status =0 order by create_at desc limit #{offset},#{limit}")
    List<PmsProduct> getMaxCreateProduct(@Param("offset") int offset,@Param("limit") int limit);
    /**
     * 获取按销量排行前 n个 个商品
     */
    @Select("select id,sale from pms_product where delete_status = 0 order by sale desc limit #{offset},#{limit}")
    List<PmsProduct> getMaxSaleProduct(@Param("offset") int offset,@Param("limit") int limit);
}
