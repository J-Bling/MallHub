package com.mall.portal.service;

import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponHistory;
import com.mall.portal.domain.model.flash.CouponHistoryDetail;
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
    List<CouponHistoryDetail> listCart(List<PmsProduct> productList, Integer type);
    /**
     * 获取当前商品相关优惠券
     */
    List<SmsCoupon> listByProduct(Long productId);
    /**
     * 获取使用状态优惠券列表
     */
    List<SmsCoupon> list(Integer useStatus);
    /**
     * 获取该用户所有领取优惠券
     */
    List<UserCoupons> getUserCoupons();

    class UserCoupons{
        private SmsCoupon coupon;
        private SmsCouponHistory couponHistory;

        public SmsCoupon getCoupon() {
            return coupon;
        }

        public SmsCouponHistory getCouponHistory() {
            return couponHistory;
        }

        public void setCoupon(SmsCoupon coupon) {
            this.coupon = coupon;
        }

        public void setCouponHistory(SmsCouponHistory couponHistory) {
            this.couponHistory = couponHistory;
        }
    }
}
