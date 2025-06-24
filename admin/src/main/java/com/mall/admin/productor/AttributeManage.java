package com.mall.admin.productor;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AttributeManage extends RabbitManage{
    void delAttributeAllByCategory(long categoryId) throws JsonProcessingException;
    void delAttribute(long attributeId,long categoryId) throws JsonProcessingException;
    void delAttributeCategoryById(long categoryId) throws JsonProcessingException;
    void delAttributeCategoryAll() throws JsonProcessingException;
}
