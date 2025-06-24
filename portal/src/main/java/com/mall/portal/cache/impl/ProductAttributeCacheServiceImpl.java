package com.mall.portal.cache.impl;

import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.mall.mbg.mapper.PmsProductAttributeMapper;
import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsProductAttributeCategory;
import com.mall.mbg.model.PmsProductAttributeCategoryExample;
import com.mall.mbg.model.PmsProductAttributeExample;
import com.mall.portal.cache.ProductAttributeCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductAttributeCacheServiceImpl implements ProductAttributeCacheService {

    @Autowired private PmsProductAttributeMapper attributeMapper;
    @Autowired private PmsProductAttributeCategoryMapper categoryMapper;
    @Autowired private RedisService redisService;

    private final Logger logger = LoggerFactory.getLogger(ProductAttributeCacheServiceImpl.class);

    @Override
    public PmsProductAttributeCategory getAttributeCategory(long categoryId) {
        return this.getCategoryCache(categoryId,1);
    }

    private PmsProductAttributeCategory getCategoryCache(long categoryId,int tryCount){
        if (tryCount>=retryCount){
            return null;
        }
        PmsProductAttributeCategory category=(PmsProductAttributeCategory) redisService.hGet(CacheKeys.ProductAttributeCategoryHashKey,CacheKeys.Field(categoryId));
        if (category!=null){
            return category;
        }
        Boolean isLock = redisService.setNX(CacheKeys.CategoryLock(categoryId),lockValue,defaultLockExpired);
        if (isLock==null || !isLock){
            try{
                Thread.sleep(defaultLockTime);
            }catch (InterruptedException interruptedException){
                logger.error("线程休眠失败:{}", interruptedException.getMessage());
            }
            return this.getCategoryCache(categoryId,tryCount+1);
        }
        category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category !=null){
            redisService.hSet(CacheKeys.ProductAttributeCategoryHashKey,CacheKeys.Field(categoryId),category);
        }
        return category;
    }

    @Override
    public List<PmsProductAttributeCategory> getCategoryList() {
        List<PmsProductAttributeCategory> categoryList = new ArrayList<>();
        try {
            Map<Object,Object> map=redisService.hGetAll(CacheKeys.ProductAttributeCategoryHashKey);
            if (map == null || map.isEmpty()) {
                PmsProductAttributeCategoryExample categoryExample = new PmsProductAttributeCategoryExample();
                categoryList = categoryMapper.selectByExample(categoryExample);
                if (categoryList != null && !categoryList.isEmpty()) {
                    Map<String, PmsProductAttributeCategory> categoryMap = new HashMap<>();
                    for (PmsProductAttributeCategory category : categoryList) {
                        categoryMap.put("" + category.getId(), category);
                    }
                    redisService.hSetAll(CacheKeys.ProductAttributeCategoryHashKey, categoryMap);
                }
                return categoryList;
            }

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                categoryList.add((PmsProductAttributeCategory) entry.getValue());
            }
        }catch (Exception e){
            logger.error("获取所有商品属性类型失败:{}",e.getMessage());
        }
        return categoryList;
    }

    @Override
    public PmsProductAttribute getAttribute(long attributeId) {
        PmsProductAttribute attribute =(PmsProductAttribute) redisService.get(CacheKeys.ProductAttributeKey(attributeId));
        if (attribute!=null){
            return attribute;
        }
        attribute = attributeMapper.selectByPrimaryKey(attributeId);
        if (attribute!=null){
            redisService.set(CacheKeys.ProductAttributeKey(attributeId),attribute);
        }
        return attribute;
    }

    @Override
    public List<PmsProductAttribute> getAttributeList(long categoryId) {
        List<PmsProductAttribute> attributeList = new ArrayList<>();
        Map<Object,Object> map = redisService.hGetAll(CacheKeys.ProductAttributeHashKey(categoryId));
        if (map==null || map.isEmpty()){
            PmsProductAttributeExample example = new PmsProductAttributeExample();
            example.createCriteria().andProductAttributeCategoryIdEqualTo(categoryId);
            attributeList = attributeMapper.selectByExample(example);
            if (attributeList!=null && !attributeList.isEmpty()){
                Map<String,Long> attributeMap = new HashMap<>();
                for (PmsProductAttribute attribute : attributeList){
                    attributeMap.put(""+attribute.getId(),attribute.getId());
                }
                redisService.hSetAll(CacheKeys.ProductAttributeHashKey(categoryId),attributeMap);
            }
        }else {
            for (Map.Entry<Object,Object> entry : map.entrySet()){
                attributeList.add((PmsProductAttribute) redisService.get(CacheKeys.ProductAttributeKey((Long) entry.getValue())));
            }
        }
        return attributeList;
    }

    @Override
    public void delAttributeAllByCategory(long categoryId) {
        redisService.del(CacheKeys.ProductAttributeHashKey(categoryId));
    }

    @Override
    public void delAttributeById(long id, long categoryId) {
        String key = CacheKeys.ProductAttributeHashKey(categoryId);
        redisService.hDel(key,CacheKeys.Field(id));
        redisService.del(CacheKeys.ProductAttributeKey(id));
    }

    @Override
    public void delAttributeCategoryById(long id) {
        redisService.hDel(CacheKeys.ProductAttributeCategoryHashKey,CacheKeys.Field(id));
    }

    @Override
    public void delAttributeCategoryAll() {
        redisService.del(CacheKeys.ProductAttributeCategoryHashKey);
    }

}
