package com.mall.admin.productor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.admin.productor.PromotionManage;
import com.mall.common.constant.enums.queues.QueueEnum;
import com.mall.common.constant.enums.rpc.PromotionRpc;
import com.mall.common.domain.RabbitRpcPromotionMessage;
import com.mall.common.domain.ReSetPromotionModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionManageIMpl implements PromotionManage {
    @Autowired private RabbitTemplate rabbitTemplate;


    @Override
    public void setNextPromotion(ReSetPromotionModel promotionModel) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PROMOTION_HANDLE.getExchange(),
                QueueEnum.QUEUE_PROMOTION_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcPromotionMessage.Builder()
                                .addPromotionModel(promotionModel)
                                .addQueue(QueueEnum.QUEUE_PROMOTION_HANDLE.getQueueName())
                                .addMethod(PromotionRpc.NEXT_PROMOTION.getMethod())
                ),
                setCallBack(PromotionRpc.NEXT_PROMOTION.getMethod())
        );
    }

    @Override
    public void incrementProductStock(long productRelationId, long skuRelationId, int count) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PROMOTION_HANDLE.getExchange(),
                QueueEnum.QUEUE_PROMOTION_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcPromotionMessage.Builder()
                                .addProductRelationId(productRelationId)
                                .addSkuRelationId(skuRelationId)
                                .addCount(count)
                                .addQueue(QueueEnum.QUEUE_PROMOTION_HANDLE.getQueueName())
                                .addMethod(PromotionRpc.INCREMENT_PRODUCT_STOCK.getMethod())
                ),
                setCallBack(PromotionRpc.INCREMENT_PRODUCT_STOCK.getMethod())
        );
    }

    @Override
    public void delProduct(long sessionId, long productId, long productRelationId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PROMOTION_HANDLE.getExchange(),
                QueueEnum.QUEUE_PROMOTION_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcPromotionMessage.Builder()
                                .addSessionId(sessionId)
                                .addProductId(productId)
                                .addProductRelationId(productRelationId)
                                .addQueue(QueueEnum.QUEUE_PROMOTION_HANDLE.getQueueName())
                                .addMethod(PromotionRpc.DEL_PRODUCT.getMethod())
                ),
                setCallBack(PromotionRpc.DEL_PRODUCT.getMethod())
        );
    }

    @Override
    public void delSession(long sessionId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PROMOTION_HANDLE.getExchange(),
                QueueEnum.QUEUE_PROMOTION_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcPromotionMessage.Builder()
                                .addSessionId(sessionId)
                                .addQueue(QueueEnum.QUEUE_PROMOTION_HANDLE.getQueueName())
                                .addMethod(PromotionRpc.DEL_SESSION.getMethod())
                ),
                setCallBack(PromotionRpc.DEL_SESSION.getMethod())
        );
    }
}