package com.mall.portal.consumer;

public interface AttributeHandle {
    void delAttributeAllByCategory(long categoryId);
    void delAttribute(long attributeId,long categoryId);
    void delAttributeCategoryById(long categoryId);
    void delAttributeCategoryAll();
}
