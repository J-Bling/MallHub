package com.mall.portal.cache.impl;

import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.PmsProductCategoryMapper;
import com.mall.mbg.model.PmsProductCategory;
import com.mall.mbg.model.PmsProductCategoryExample;
import com.mall.portal.cache.ProductCategoryCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductCategoryCacheServiceImpl implements ProductCategoryCacheService {
    @Autowired private PmsProductCategoryMapper categoryMapper;
    @Autowired private RedisService redisService;
    private final Logger logger = LoggerFactory.getLogger(ProductCategoryCacheServiceImpl.class);

    @Value("${redis.key.ProductCategoryKey:product-category-key}")
    private String ProductCategoryKey;

    @Override
    public void addCategory(long id) {
        PmsProductCategory category = categoryMapper.selectByPrimaryKey(id);
        if (category!=null){
            redisService.hSet(ProductCategoryKey,HashField(category.getId()),category);
        }
    }

    @Override
    public void delCategory(long id) {
        redisService.hDel(ProductCategoryKey,HashField(id));
    }

    @Override
    public void cleanCache() {
        redisService.del(ProductCategoryKey);
    }

    @Override
    public PmsProductCategory get(long id) {
        PmsProductCategory category =null;
        try {
            category = (PmsProductCategory) redisService.hGet(ProductCategoryKey, HashField(id));
            if (category == null) {
                category = categoryMapper.selectByPrimaryKey(id);
                if (category != null) {
                    redisService.hSet(ProductCategoryKey, HashField(id), category);
                }
            }
        }catch (Exception e){
            logger.error("获取单个商品类型失败 id ：{},原因:{}",id,e.getMessage());
        }
        return category;
    }

    @Override
    public List<PmsProductCategory> getAll() {
        List<PmsProductCategory> categoryList = new ArrayList<>();
        try {
            Map<Object, Object> resultMap = redisService.hGetAll(ProductCategoryKey);
            if (resultMap == null) {
                PmsProductCategoryExample example = new PmsProductCategoryExample();
                categoryList = categoryMapper.selectByExample(example);
                if (categoryList != null && !categoryList.isEmpty()) {
                    Map<String, PmsProductCategory> categoryMap = new HashMap<>();
                    for (PmsProductCategory category : categoryList) {
                        categoryMap.put(category.getId() + "", category);
                    }
                    redisService.hSetAll(ProductCategoryKey, categoryMap);
                }
                return categoryList;
            }
            for (Map.Entry<Object, Object> entry : resultMap.entrySet()) {
                categoryList.add((PmsProductCategory) entry.getValue());
            }
        }catch (Exception e){
            logger.error("获取所有商品类型失败:{}",e.getMessage());
        }
        return categoryList;
    }
}
