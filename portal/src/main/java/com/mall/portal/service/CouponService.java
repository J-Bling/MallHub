package com.mall.portal.service;

import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponHistory;
import com.mall.portal.domain.model.PromotionCartItem;
import com.mall.portal.domain.model.CouponHistoryDetail;
import java.util.List;

public interface CouponService {
    /**
     * 会员添加优惠券
     */
    void add(Long couponId);
    /**
     * 获取优惠券历史列表
     */
    List<SmsCouponHistory> listHistory(Integer useStatus);
    /**
     * 根据购物车信息获取可用优惠券 使用可用:0->不可用；1->可用
     */
    List<CouponHistoryDetail> listCart(List<PromotionCartItem> cartItemList, Integer type);

    /**
     * 获取当前商品相关优惠券
     */
    List<SmsCoupon> listByProduct(Long productId);

    /**
     * 获取用户优惠券列表
     */
    List<SmsCoupon> list(Integer useStatus);
}
