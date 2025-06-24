package com.mall.admin.cache.impl;

import com.mall.admin.cache.ProductCacheManage;
import com.mall.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductCacheManageImpl implements ProductCacheManage {
    @Autowired
    private RedisService redisService;

    @Value("${redis.key.ProductCategoryKey:product-category-key}")
    private String ProductCategoryKey;


    @Override
    public void deleteCateCache(Long cateId) {
        redisService.hDel(ProductCategoryKey,""+cateId);
    }

    @Override
    public void cleanCateCache() {
        redisService.del(ProductCategoryKey);
    }

}
