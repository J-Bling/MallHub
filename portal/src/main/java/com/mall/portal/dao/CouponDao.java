package com.mall.portal.dao;

import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponHistory;
import com.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.mall.mbg.model.SmsCouponProductRelation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CouponDao {
    // 更新优惠券库存
    @Update("UPDATE sms_coupon SET " +
            "count = count + #{countDelta}, " +
            "receive_count = receive_count + #{receiveCountDelta} " +
            "WHERE id = #{couponId}")
    void updateCouponCountReceiveCount(
            @Param("couponId") Long couponId,
            @Param("countDelta") Integer countDelta,
            @Param("receiveCountDelta") Integer receiveCountDelta
    );

    // 统计用户领取的优惠券数量
    @Select("SELECT COUNT(*) FROM sms_coupon_history " +
            "WHERE member_id = #{memberId} AND coupon_id = #{couponId}")
    int countMemberCouponHistory(
            @Param("memberId") Long memberId,
            @Param("couponId") Long couponId
    );

    // 查询用户优惠券领取记录
    List<SmsCouponHistory> selectCouponHistoryByMember(
            @Param("memberId") Long memberId,
            @Param("useStatus") Integer useStatus
    );

    // 查询优惠券关联的商品
    @Select("SELECT * FROM sms_coupon_product_relation WHERE coupon_id = #{couponId}")
    List<SmsCouponProductRelation> selectProductRelationsByCoupon(
            @Param("couponId") Long couponId
    );

    // 查询优惠券关联的商品分类
    @Select("SELECT * FROM sms_coupon_product_category_relation WHERE coupon_id = #{couponId}")
    List<SmsCouponProductCategoryRelation> selectCategoryRelationsByCoupon(
            @Param("couponId") Long couponId
    );

    // 查询商品可用的优惠券
    List<SmsCoupon> selectCouponsForProduct(
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId
    );

    // 查询用户指定使用状态的优惠券
    @Select("SELECT c.* FROM sms_coupon c " +
            "JOIN sms_coupon_history h ON c.id = h.coupon_id " +
            "WHERE h.member_id = #{memberId} AND h.use_status = #{useStatus}")
    List<SmsCoupon> selectCouponsByUseStatus(
            @Param("memberId") Long memberId,
            @Param("useStatus") Integer useStatus
    );
}
