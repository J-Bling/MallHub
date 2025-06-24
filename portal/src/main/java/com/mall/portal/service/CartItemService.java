package com.mall.portal.service;

import com.mall.mbg.model.OmsCartItem;
import com.mall.portal.domain.model.product.CartProduct;
import com.mall.portal.domain.model.product.PromotionCartItem;
import java.util.List;

/**
 * 购物车管理
 */
public interface CartItemService {
    /**
     * 查询购物车也没有该商品 有就增加数量 没有就加入购物车
     */
    void add(OmsCartItem cartItem);
    /**
     * 分页获取当前用户的购物车列表
     */
    List<OmsCartItem> list();
    /**
     * 获取包含促销活动信息的购物车列表 计算购物车中的商品 库存 减免 促销活动 赠送积分和 成长值 信息
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
