package com.mall.portal.dao;

import com.mall.mbg.model.SmsCoupon;
import com.mall.portal.domain.model.flash.CouponHistoryDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CouponDao {
    @Update("update sms_coupon set count = count + #{incrementCount} , receive_count = receive_count + #{incrementReceiveCount} where id =#{couponId}")
    void updateCouponCountReceiveCount(@Param("couponId") long couponId,@Param("incrementCount") int incrementCount, @Param("incrementReceiveCount") int incrementReceiveCount);

    List<SmsCoupon> getListByUseStatus(@Param("userId") long userId ,@Param("useStatus") int useStatus);

    List<CouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);
}
