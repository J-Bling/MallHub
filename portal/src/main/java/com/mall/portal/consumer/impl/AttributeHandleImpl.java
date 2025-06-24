package com.mall.portal.consumer.impl;

import com.mall.portal.cache.ProductAttributeCacheService;
import com.mall.portal.consumer.AttributeHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeHandleImpl implements AttributeHandle {
    @Autowired private ProductAttributeCacheService attributeCacheService;


    @Override
    public void delAttributeAllByCategory(long categoryId) {
        attributeCacheService.delAttributeAllByCategory(categoryId);
    }

    @Override
    public void delAttribute(long id, long categoryId) {
        attributeCacheService.delAttributeById(id,categoryId);
    }

    @Override
    public void delAttributeCategoryById(long categoryId) {
        attributeCacheService.delAttributeCategoryById(categoryId);
    }

    @Override
    public void delAttributeCategoryAll() {
        attributeCacheService.delAttributeCategoryAll();
    }
}
