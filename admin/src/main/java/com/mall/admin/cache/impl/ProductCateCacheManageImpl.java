package com.mall.admin.cache.impl;

import com.mall.admin.cache.ProductCateCacheManage;
import com.mall.common.service.RedisService;
import com.mall.mbg.model.PmsProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductCateCacheManageImpl implements ProductCateCacheManage {
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

    @Override
    public void add(PmsProductCategory category) {
        redisService.hSet(ProductCategoryKey,""+category.getId(),category);
    }

    @Override
    public void addAdd(List<PmsProductCategory> categoryList) {
        Map<String,PmsProductCategory> categoryMap = new HashMap<>();
        for (PmsProductCategory category : categoryList){
            categoryMap.put(""+category.getId(),category);
        }
        redisService.hSetAll(ProductCategoryKey,categoryMap);
    }
}
