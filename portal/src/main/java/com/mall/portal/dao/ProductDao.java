package com.mall.portal.dao;

import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.SmsCoupon;
import com.mall.portal.domain.model.CartProduct;
import com.mall.portal.domain.model.PromotionProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 购物车管理dao
 */
public interface ProductDao {
    /**
     * 获取购物车商品信息
     */
    CartProduct getCartProduct(@Param("id") Long id);

    /**
     * 获取促销商品信息列表
     */
    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);

    /**
     * 获取可用优惠券列表
     */
    List<SmsCoupon> getAvailableCouponList(@Param("productId") Long productId, @Param("productCategoryId") Long productCategoryId);
    /**
     * 根据品牌获取相关商品
     */
    @Select("select * from pms_product where brand_id = #{brandId} order id asc limit #{offset},#{limit}")
    List<PmsProduct> getProductByBrandId(@Param("brandId") long brandId,@Param("offset") int offset,@Param("limit") int limit);
}
