package com.mall.portal.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.constant.enums.rpc.CouponRpc;
import com.mall.common.domain.RabbitCouponMessage;
import com.mall.portal.consumer.CouponMqHandle;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "mall.coupon.queue")
public class RpcCouponService {
    @Autowired private CouponMqHandle handle;
    @Autowired private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RpcCouponService.class);

    @RabbitHandler
    public void handler(Message message , Channel channel) throws IOException {
        String body = new String(message.getBody());
        try {
            RabbitCouponMessage couponMessage = objectMapper.readValue(body,objectMapper.constructType(RabbitCouponMessage.class));
            String method = couponMessage.getMethod();
            if (CouponRpc.DEL_COUPON.getMethod().equals(method)){
                boolean is = couponMessage.getAllUseType()!=null && couponMessage.getAllUseType();
                handle.delCacheCoupon(couponMessage.getCouponId(), is);
            }else if (CouponRpc.DEL_COUPON_PRODUCT_RELATION.getMethod().equals(method)){
                handle.delCacheCouponProductRelation(couponMessage.getProductId(),couponMessage.getCouponId());
            } else if (CouponRpc.DEL_COUPON_PRO_CATE_RELATION.getMethod().equals(method)) {
                handle.delCacheCouponProductCategoryRelation(couponMessage.getProductCateId(),couponMessage.getCouponId());
            }else {
                throw new RuntimeException("没有该方法");
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            logger.error("处理优惠券调用失败:{}",e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }
    }
}
