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
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }


    @Override
    public void delRank() throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PRODUCT_HANDLE,
                ProductRpc.DEL_RANK.getMethod(),
                builder->{},
                new RabbitRpcProductMassage()
        );
    }

    @Override
    public void addProduct(long productId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PRODUCT_HANDLE,
                ProductRpc.ADD_PRODUCT.getMethod(),
                builder->builder.setProductId(productId),
                new RabbitRpcProductMassage()
        );
    }

    @Override
    public void deleteProduct(long productId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PRODUCT_HANDLE,
                ProductRpc.DEL_PRODUCT.getMethod(),
                builder->builder.setProductId(productId),
                new RabbitRpcProductMassage()
        );
    }

    @Override
    public void upToDelProductCache(long productId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PRODUCT_HANDLE,
                ProductRpc.UP_DEL_PRODUCT.getMethod(),
                builder->builder.setProductId(productId),
                new RabbitRpcProductMassage()
        );
    }

    @Override
    public void upToDelProductSubModelCache(long productId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PRODUCT_HANDLE,
                ProductRpc.UP_DEL_PRODUCT_SUB.getMethod(),
                builder->builder.setProductId(productId),
                new RabbitRpcProductMassage()
        );
    }

    @Override
    public void upToDelSkuStockCache(long productId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PRODUCT_HANDLE,
                ProductRpc.UP_DEL_SKU.getMethod(),
                builder->builder.setProductId(productId),
                new RabbitRpcProductMassage()
        );
    }

    @Override
    public void upToDelStats(long productId, long skuId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.QUEUE_PRODUCT_HANDLE,
                ProductRpc.UP_DEL_STATS.getMethod(),
                builder -> builder.setProductId(productId),
                new RabbitRpcProductMassage()
        );
    }
}