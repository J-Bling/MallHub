package com.mall.admin.productor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.admin.productor.AttributeManage;
import com.mall.common.constant.enums.queues.QueueEnum;
import com.mall.common.constant.enums.rpc.AttributeRpc;
import com.mall.common.domain.RabbitAttributeMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeManageImpl implements AttributeManage {
    @Autowired private RabbitTemplate rabbitTemplate;


    @Override
    public void delAttributeAllByCategory(long categoryId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.PRODUCT_ATTRIBUTE_HANDLE,
                AttributeRpc.DEL_ATTRIBUTE_ALL_BY_CATEGORY.getMethod(),
                builder->builder.setCategoryId(categoryId),
                new RabbitAttributeMessage()
        );
    }

    @Override
    public void delAttribute(long attributeId, long categoryId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.PRODUCT_ATTRIBUTE_HANDLE,
                AttributeRpc.DEL_ATTRIBUTE.getMethod(),
                builder->{
                    builder.setCategoryId(categoryId);
                    builder.setAttributeId(attributeId);
                },
                new RabbitAttributeMessage()
        );
    }

    @Override
    public void delAttributeCategoryById(long categoryId) throws JsonProcessingException {
        sendMessage(
                QueueEnum.PRODUCT_ATTRIBUTE_HANDLE,
                AttributeRpc.DEL_ATTRIBUTE_CATEGORY_BY_ID.getMethod(),
                builder->{
                    builder.setCategoryId(categoryId);
                },
                new RabbitAttributeMessage()
        );
    }

    @Override
    public void delAttributeCategoryAll() throws JsonProcessingException {
        sendMessage(
                QueueEnum.PRODUCT_ATTRIBUTE_HANDLE,
                AttributeRpc.DEL_ATTRIBUTE_CATEGORY_ALL.getMethod(),
                builder->{},
                new RabbitAttributeMessage()
        );
    }

    @Override
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }
}
