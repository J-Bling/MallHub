package com.mall.portal.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.constant.enums.rpc.AttributeRpc;
import com.mall.common.domain.RabbitAttributeMessage;
import com.mall.portal.consumer.AttributeHandle;
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
@RabbitListener(queues = "mall.attribute.queue")
public class RpcAttributeService {
    @Autowired private AttributeHandle handle;
    @Autowired private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RpcAttributeService.class);

    @RabbitHandler
    public void handler(Message message, Channel channel) throws IOException {
        String body = new String(message.getBody());
        try{
            RabbitAttributeMessage attributeMessage = objectMapper.readValue(body,objectMapper.constructType(RabbitAttributeMessage.class));
            String method = attributeMessage.getMethod();
            if (AttributeRpc.DEL_ATTRIBUTE.getMethod().equals(method)){
                handle.delAttribute(attributeMessage.getAttributeId(),attributeMessage.getCategoryId());
            } else if (AttributeRpc.DEL_ATTRIBUTE_CATEGORY_BY_ID.getMethod().equals(method)) {
                handle.delAttributeCategoryById(attributeMessage.getCategoryId());
            } else if (AttributeRpc.DEL_ATTRIBUTE_ALL_BY_CATEGORY.getMethod().equals(method)) {
                handle.delAttributeAllByCategory(attributeMessage.getCategoryId());
            } else if (AttributeRpc.DEL_ATTRIBUTE_CATEGORY_ALL.getMethod().equals(method)) {
                handle.delAttributeCategoryAll();
            }else {
                throw new RuntimeException("没有该方法可以调用");
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        }catch (Exception e){
            logger.error("调用服务失败:{}",e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }
    }
}
