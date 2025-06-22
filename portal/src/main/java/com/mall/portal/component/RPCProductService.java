package com.mall.portal.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.constant.enums.rpc.ProductRpc;
import com.mall.common.domain.RabbitRpcProductMassage;
import com.mall.portal.consumer.ProductMqHandle;
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
@RabbitListener(queues = "mall.product.handle")
public class RPCProductService {
    @Autowired private ProductMqHandle productMqHandle;
    @Autowired private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RPCProductService.class);

    @RabbitHandler
    public void handle(Message message, Channel channel) throws IOException {
        String body = new String(message.getBody());
        try{
            RabbitRpcProductMassage rabbitRpcProductMassage = objectMapper.readValue(body,objectMapper.constructType(RabbitRpcProductMassage.class));
            String method = rabbitRpcProductMassage.getMethod();
            if (ProductRpc.DEL_RANK.getMethod().equals(method)){
                productMqHandle.delRank();
            }else if (ProductRpc.DEL_PRODUCT.getMethod().equals(method)){
                productMqHandle.deleteProduct(rabbitRpcProductMassage.getProductId());
            } else if (ProductRpc.ADD_PRODUCT.getMethod().equals(method)) {
                productMqHandle.addProduct(rabbitRpcProductMassage.getProductId());
            } else if (ProductRpc.UP_DEL_PRODUCT.getMethod().equals(method)) {
                productMqHandle.upToDelProductCache(rabbitRpcProductMassage.getProductId());
            } else if (ProductRpc.UP_DEL_PRODUCT_SUB.getMethod().equals(method)) {
                productMqHandle.upToDelProductSubModelCache(rabbitRpcProductMassage.getProductId());
            } else if (ProductRpc.UP_DEL_SKU.getMethod().equals(method)) {
                productMqHandle.upToDelSkuStockCache(rabbitRpcProductMassage.getProductId());
            } else if (ProductRpc.UP_DEL_STATS.getMethod().equals(method)) {
                productMqHandle.upToDelStats(rabbitRpcProductMassage.getProductId(), rabbitRpcProductMassage.getSkuId());
            }else {
                throw new RuntimeException("没有该方法:"+method);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);

        }catch (Exception e){
            logger.error("调用方法失败:body:{};错误:{}",body,e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }
}
