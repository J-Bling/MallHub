package com.mall.portal.cache.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.mall.mbg.mapper.PmsProductAttributeMapper;
import com.mall.mbg.model.*;
import com.mall.portal.cache.ProductAttributeCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductAttributeCacheServiceImpl implements ProductAttributeCacheService {

    @Autowired private PmsProductAttributeMapper attributeMapper;
    @Autowired private PmsProductAttributeCategoryMapper categoryMapper;
    @Autowired private RedisService redisService;
    @Autowired private CounterRedisService counterRedisService;

    private final Logger logger = LoggerFactory.getLogger(ProductAttributeCacheServiceImpl.class);

    @Override
    public void addAttributeCate(long cateId) {
        PmsProductAttributeCategory category = categoryMapper.selectByPrimaryKey(cateId);
        if (category==null){
            return;
        }
        redisService.hSet(CacheKeys.ProductAttributeCategoryHashKey,CacheKeys.Field(cateId),category);
    }

    @Override
    public void addAttribute(long attributeId) {
        PmsProductAttribute attribute = attributeMapper.selectByPrimaryKey(attributeId);
        if (attribute==null){
            return;
        }
        counterRedisService.sAdd(CacheKeys.AttributeCateSetKey(attribute.getProductAttributeCategoryId()),attributeId+"");
        redisService.set(CacheKeys.ProductAttributeKey(attributeId),attribute,oneDayExpired);
    }

    @Override
    public void addAttributeAll(List<Long> attributeIds) {
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andIdIn(attributeIds);
        List<PmsProductAttribute> attributeList = attributeMapper.selectByExample(example);
        if (attributeList == null || attributeList.isEmpty()){
            return;
        }
        Map<String,List<String>> listMap = new HashMap<>();
        for (PmsProductAttribute attribute : attributeList){
            listMap.computeIfAbsent(CacheKeys.AttributeCateSetKey(attribute.getProductAttributeCategoryId()),k->new ArrayList<>()).add(attribute.getId()+"");
            redisService.set(CacheKeys.ProductAttributeKey(attribute.getId()),attribute,oneDayExpired);
        }
        for (Map.Entry<String,List<String>> entry : listMap.entrySet()){
            counterRedisService.sAddAll(entry.getKey(),entry.getValue());
        }
    }



    @Override
    public PmsProductAttributeCategory getAttributeCategory(long categoryId) {
        PmsProductAttributeCategory category=(PmsProductAttributeCategory) redisService.hGet(CacheKeys.ProductAttributeCategoryHashKey,CacheKeys.Field(categoryId));
        if (category!=null){
            return category;
        }
        for (int i=0;i<retryCount;i++){
            Boolean isLock = redisService.setNX(CacheKeys.CategoryLock(categoryId),lockValue,defaultLockExpired);
            if (isLock ==null || !isLock){
                try{
                    Thread.sleep(defaultLockTime);
                }catch (InterruptedException interruptedException){
                    logger.error("线程休眠失败:{}", interruptedException.getMessage());
                }
                continue;
            }
            category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category !=null){
                redisService.hSet(CacheKeys.ProductAttributeCategoryHashKey,CacheKeys.Field(categoryId),category);
            }
            break;
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
            redisService.set(CacheKeys.ProductAttributeKey(attributeId),attribute,oneDayExpired);
            counterRedisService.sAdd(CacheKeys.AttributeCateSetKey(attribute.getProductAttributeCategoryId()),attributeId+"");
        }
        return attribute;
    }

    @Override
    public List<PmsProductAttribute> getAttributeList(long categoryId) {
        Set<String> strings = counterRedisService.sMembers(CacheKeys.AttributeCateSetKey(categoryId));
        List<PmsProductAttribute> attributeList = new ArrayList<>();
        if (strings!=null && !strings.isEmpty()){
            for (String id : strings){
                attributeList.add(this.getAttribute(Long.parseLong(id)));
            }
        }else {
            PmsProductAttributeExample example = new PmsProductAttributeExample();
            example.createCriteria().andProductAttributeCategoryIdEqualTo(categoryId);
            attributeList = attributeMapper.selectByExample(example);
            if (attributeList!=null && !attributeList.isEmpty()){
                List<String> ids = new ArrayList<>();
                for (PmsProductAttribute attribute : attributeList){
                    ids.add(""+attribute.getId());
                    redisService.set(CacheKeys.ProductAttributeKey(attribute.getId()),attribute,oneDayExpired);
                }
                counterRedisService.sAddAll(CacheKeys.AttributeCateSetKey(categoryId),ids);
            }
        }
        return attributeList;
    }


    @Override
    public void delAttributeAllByCategory(long categoryId) {
        redisService.del(CacheKeys.AttributeCateSetKey(categoryId));
    }

    @Override
    public void delAttributeById(long id, long categoryId) {
        redisService.del(CacheKeys.ProductAttributeKey(id));
        counterRedisService.sRm(CacheKeys.AttributeCateSetKey(categoryId),""+id);
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
