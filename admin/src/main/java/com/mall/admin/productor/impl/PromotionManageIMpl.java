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
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }



    @Override
    public void setNextPromotion(ReSetPromotionModel promotionModel) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PROMOTION_HANDLE,
                PromotionRpc.NEXT_PROMOTION.getMethod(),
                builder -> builder.addPromotionModel(promotionModel),
                RabbitRpcPromotionMessage.Builder()
        );
    }

    @Override
    public void incrementProductStock(long productRelationId, long skuRelationId, int count) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PROMOTION_HANDLE,
                PromotionRpc.INCREMENT_PRODUCT_STOCK.getMethod(),
                builder -> builder
                        .addProductRelationId(productRelationId)
                        .addSkuRelationId(skuRelationId)
                        .addCount(count),
                RabbitRpcPromotionMessage.Builder()
        );
    }

    @Override
    public void delProduct(long sessionId, long productId, long productRelationId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PROMOTION_HANDLE,
                PromotionRpc.DEL_PRODUCT.getMethod(),
                builder -> builder
                        .addProductRelationId(productRelationId)
                        .addSessionId(sessionId)
                        .addProductId(productId),
                RabbitRpcPromotionMessage.Builder()
        );
    }

    @Override
    public void delSession(long sessionId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PROMOTION_HANDLE,
                PromotionRpc.DEL_PRODUCT.getMethod(),
                builder -> builder
                        .addSessionId(sessionId),
                RabbitRpcPromotionMessage.Builder()
        );
    }
}