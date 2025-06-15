package com.mall.common.service;

import java.util.List;
import java.util.Map;

public interface CounterRedisService {
    /**
     * 判断Hash结构中是否有该属性
     */
    Boolean hHasKey(String key, String hashKey);
    /**
     * Hash结构中属性递增
     */
    Long hInCr(String key, String hashKey, long delta);
    /**
     * Hash结构中属性递减
     */
    Long hDeCr(String key, String hashKey, long delta);
    /**
     * 获取Hash结构中的属性
     */
    String hGet(String key,String field);
    /**
     * 向Hash结构中放入一个属性
     */
    void hSet(String key,String field,String value);
    /**
     * 向Hash结构中放入一个属性
     */
    void hSet(String key,String field,String value,long expired);
    /**
     * 直接获取整个Hash结构
     */
    Map<String,String> hGetAll(String key);
    /**
     * 直接设置整个Hash结构
     */
    void hSetAll(String key,Map<String,String> fieldValues);
    /**
     * 直接设置整个Hash结构
     */
    void hSetAll(String key,Map<String,String> fieldValues,long expired);
    /**
     * 删除hash结构
     */
    void hDel(String key,String... fields);
    /**
     * 删除字段
     */
    Boolean del(String key);
    /**
     * 设置有效时间
     */
    Long del(List<String> keys);
}
