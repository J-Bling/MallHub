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
     * 根据新品(3天前)排行分页获取 product
     */
    @Select("select * from pms_product where create_at > #{created} order by create_at desc limit #{offset},#{limit}")
    List<PmsProduct> getProductOfCreate(@Param("offset") int offset, @Param("limit") int limit, @Param("created") long created);
}
