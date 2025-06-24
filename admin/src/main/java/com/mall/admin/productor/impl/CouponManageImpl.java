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
    public void delCoupon(Long id, Boolean isAllUseType) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.COUPON_HANDLE.getExchange(),
                QueueEnum.COUPON_HANDLE.getRouteKey(),
                serialization(
                        RabbitCouponMessage.builder()
                                .couponId(id)
                                .isAllUseType(isAllUseType)
                                .build()
                                .addQueue(QueueEnum.COUPON_HANDLE.getQueueName())
                                .addMethod(CouponRpc.DEL_COUPON.getMethod())
                ),
                setCallBack(CouponRpc.DEL_COUPON.getMethod())
        );
    }

    @Override
    public void delCacheCouponProductRelation(Long productId, Long couponId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.COUPON_HANDLE.getExchange(),
                QueueEnum.COUPON_HANDLE.getRouteKey(),
                serialization(
                        RabbitCouponMessage.builder()
                                .couponId(couponId)
                                .productId(productId)
                                .build()
                                .addQueue(QueueEnum.COUPON_HANDLE.getQueueName())
                                .addMethod(CouponRpc.DEL_COUPON_PRODUCT_RELATION.getMethod())
                ),
                setCallBack(CouponRpc.DEL_COUPON_PRODUCT_RELATION.getMethod())
        );
    }

    @Override
    public void delCacheCouponProductCateRelation(Long productCateId, Long couponId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.COUPON_HANDLE.getExchange(),
                QueueEnum.COUPON_HANDLE.getRouteKey(),
                serialization(
                        RabbitCouponMessage.builder()
                                .couponId(couponId)
                                .productCateId(productCateId)
                                .build()
                                .addQueue(QueueEnum.COUPON_HANDLE.getQueueName())
                                .addMethod(CouponRpc.DEL_COUPON_PRO_CATE_RELATION.getMethod())
                ),
                setCallBack(CouponRpc.DEL_COUPON_PRO_CATE_RELATION.getMethod())
        );
    }
}
