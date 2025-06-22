package com.mall.admin.productor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.admin.productor.ProductManage;
import com.mall.common.constant.enums.queues.QueueEnum;
import com.mall.common.constant.enums.rpc.ProductRpc;
import com.mall.common.domain.RabbitRpcProductMassage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductManageImpl implements ProductManage {
    @Autowired private RabbitTemplate rabbitTemplate;

    @Override
    public void delRank() throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),
                QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcProductMassage.Builder()
                            .addQueue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName())
                            .addMethod(ProductRpc.DEL_RANK.getMethod())
                ),
                setCallBack(ProductRpc.DEL_RANK.getMethod())
        );
    }

    @Override
    public void addProduct(long productId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),
                QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcProductMassage.Builder()
                                .addProductId(productId)
                                .addQueue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName())
                                .addMethod(ProductRpc.ADD_PRODUCT.getMethod())
                ),
                setCallBack(ProductRpc.ADD_PRODUCT.getMethod())
        );
    }

    @Override
    public void deleteProduct(long productId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),
                QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcProductMassage.Builder()
                                .addProductId(productId)
                                .addQueue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName())
                                .addMethod(ProductRpc.DEL_PRODUCT.getMethod())
                ),
                setCallBack(ProductRpc.DEL_PRODUCT.getMethod())
        );
    }

    @Override
    public void upToDelProductCache(long productId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),
                QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcProductMassage.Builder()
                                .addProductId(productId)
                                .addQueue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName())
                                .addMethod(ProductRpc.UP_DEL_PRODUCT.getMethod())
                ),
                setCallBack(ProductRpc.UP_DEL_PRODUCT.getMethod())
        );
    }

    @Override
    public void upToDelProductSubModelCache(long productId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),
                QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcProductMassage.Builder()
                                .addProductId(productId)
                                .addQueue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName())
                                .addMethod(ProductRpc.UP_DEL_PRODUCT_SUB.getMethod())
                ),
                setCallBack(ProductRpc.UP_DEL_PRODUCT_SUB.getMethod())
        );
    }

    @Override
    public void upToDelSkuStockCache(long productId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),
                QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcProductMassage.Builder()
                                .addProductId(productId)
                                .addQueue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName())
                                .addMethod(ProductRpc.UP_DEL_SKU.getMethod())
                ),
                setCallBack(ProductRpc.UP_DEL_SKU.getMethod())
        );
    }

    @Override
    public void upToDelStats(long productId, long skuId) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),
                QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey(),
                serialization(
                        RabbitRpcProductMassage.Builder()
                                .addProductId(productId)
                                .addQueue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName())
                                .addMethod(ProductRpc.UP_DEL_STATS.getMethod())
                ),
                setCallBack(ProductRpc.UP_DEL_STATS.getMethod())
        );
    }
}