package com.mall.admin.productor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.admin.productor.CouponManage;
import com.mall.common.constant.enums.queues.QueueEnum;
import com.mall.common.constant.enums.rpc.CouponRpc;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mall.common.domain.RabbitCouponMessage;

@Service
public class CouponManageImpl implements CouponManage {
    @Autowired private RabbitTemplate rabbitTemplate;

    @Override
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    @Override
    public void delCoupon(Long id, Boolean isAllUseType) throws JsonProcessingException {
        sendMessage(
                QueueEnum.COUPON_HANDLE,
                CouponRpc.DEL_COUPON.getMethod(),
                builder->{
                    builder.setCouponId(id);
                    builder.setIsAllUseType(isAllUseType);
                },
                new RabbitCouponMessage()
        );
    }

    @Override
    public void delCacheCouponProductRelation(Long productId, Long couponId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.COUPON_HANDLE,
                CouponRpc.DEL_COUPON_PRODUCT_RELATION.getMethod(),
                builder->{
                    builder.setCouponId(couponId);
                    builder.setProductId(productId);
                },
                new RabbitCouponMessage()
        );
    }

    @Override
    public void delCacheCouponProductCateRelation(Long productCateId, Long couponId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.COUPON_HANDLE,
                CouponRpc.DEL_COUPON_PRO_CATE_RELATION.getMethod(),
                builder->{
                    builder.setProductCateId(productCateId);
                    builder.setCouponId(couponId);
                },
                new RabbitCouponMessage()
        );
    }

}
