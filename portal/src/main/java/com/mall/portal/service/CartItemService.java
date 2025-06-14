package com.mall.portal.service;

import com.mall.mbg.model.OmsCartItem;
import com.mall.portal.domain.model.CartProduct;
import com.mall.portal.domain.model.PromotionCartItem;
import java.util.List;

/**
 * 购物车管理
 */
public interface CartItemService {
    /**
     * 增加数量，无添加到购物车
     */
    void add(OmsCartItem cartItem);

    /**
     * 分页获取当前用户的购物车列表
     */
    List<OmsCartItem> list(int pageNum , int pageSize);

    /**
     * 获取包含促销活动信息的购物车列表
     */
    List<PromotionCartItem> promotionList(List<Long> cartIds);
    /**
     * 修改某个购物车商品的数量
     */
    void updateQuantity(Long cartId,Integer quantity);

    /**
     * 批量删除购物车中的商品
     */
    void delete(List<Long> cartIds);

    /**
     *获取购物车中用于选择商品规格的商品信息
     */
    CartProduct getCartProduct(Long productId);
    /**
     * 修改购物车中商品的规格
     */
    void updateAttribute(OmsCartItem cartItem);

    /**
     * 清空购物车
     */
    void clear();
}
