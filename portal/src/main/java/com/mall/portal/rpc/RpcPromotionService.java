package com.mall.portal.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.constant.enums.rpc.PromotionRpc;
import com.mall.common.domain.RabbitRpcPromotionMessage;
import com.mall.portal.consumer.PromotionMqHandle;
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
@RabbitListener(queues = "mall.promotion.handle")
public class RpcPromotionService {
    @Autowired private PromotionMqHandle handle;
    @Autowired private ObjectMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(RpcPromotionService.class);


    @RabbitHandler
    public void handler(Message message , Channel channel) throws IOException {
        String body = new String(message.getBody());
        try {
            RabbitRpcPromotionMessage promotionMessage = mapper.readValue(body,mapper.constructType(RabbitRpcPromotionMessage.class));
            String method = promotionMessage.getMethod();
            if (PromotionRpc.NEXT_PROMOTION.getMethod().equals(method)){
                handle.setNextPromotion(promotionMessage.getPromotionModel());
            } else if (PromotionRpc.INCREMENT_PRODUCT_STOCK.getMethod().equals(method)) {
                handle.incrementProductStock(promotionMessage.getProductRelationId(), promotionMessage.getSkuRelationId(), promotionMessage.getCount());
            } else if (PromotionRpc.DEL_PRODUCT.getMethod().equals(method)) {
                handle.delProduct(promotionMessage.getSessionId(), promotionMessage.getProductId(), promotionMessage.getProductRelationId());
            } else if (PromotionRpc.DEL_SESSION.getMethod().equals(method)) {
                handle.delSession(promotionMessage.getSessionId());
            }else {
                throw new RuntimeException("没有该方法:"+method);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            logger.error("调用 promotion服务失败:body : {},原因:{}",body,e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }
    }
}
