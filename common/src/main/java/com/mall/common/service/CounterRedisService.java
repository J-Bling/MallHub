package com.mall.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CounterRedisService {
    String get(String key);
    void set(String key,String value);
    Long inCr(String key,long delta);
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
    /**
     * 向ZSet中添加元素（或更新分数）
     * @param key    ZSet的Key
     * @param value  元素值
     * @param score  分数（用于排序）
     * @return 是否添加成功（true=新增元素，false=更新已有元素）
     */
    Boolean zAdd(String key, String value, double score);

    /**
     * 批量添加ZSet元素
     * @param key     ZSet的Key
     * @param values  元素值及分数的Map（value -> score）
     * @return 新增的元素数量
     */
    Long zAddAll(String key, Map<String, Double> values);

    /**
     * 删除ZSet中的元素
     * @param key     ZSet的Key
     * @param values  要删除的元素值
     * @return 删除的元素数量
     */
    Long zRemove(String key, String... values);

    /**
     * 获取ZSet中元素的分数
     * @param key    ZSet的Key
     * @param value  元素值
     * @return 分数（如果元素不存在返回null）
     */
    Double zScore(String key, String value);

    /**
     * 获取ZSet的排名（升序排名，0表示第一名）
     * @param key    ZSet的Key
     * @param value  元素值
     * @return 排名（如果元素不存在返回null）
     */
    Long zRank(String key, String value);

    /**
     * 获取ZSet的排名（降序排名，0表示第一名）
     * @param key    ZSet的Key
     * @param value  元素值
     * @return 排名（如果元素不存在返回null）
     */
    Long zReverseRank(String key, String value);

    /**
     * 获取ZSet中指定分数范围的元素（升序）
     * @param key  ZSet的Key
     * @param min  最小分数（包含）
     * @param max  最大分数（包含）
     * @return 元素集合
     */
    Set<String> zRangeByScore(String key, double min, double max);

    /**
     * 获取ZSet中指定排名范围的元素（升序）
     * @param key  ZSet的Key
     * @param start 起始排名（0表示第一名）
     * @param end   结束排名（-1表示最后一名）
     * @return 元素集合
     */
    Set<String> zRange(String key, long start, long end);

    /**
     * 获取ZSet中指定排名范围的元素（降序）
     * @param key  ZSet的Key
     * @param start 起始排名（0表示第一名）
     * @param end   结束排名（-1表示最后一名）
     * @return 元素集合
     */
    Set<String> zReverseRange(String key, long start, long end);

    /**
     * 获取ZSet的元素数量
     * @param key  ZSet的Key
     * @return 元素总数
     */
    Long zSize(String key);

    /**
     * 删除ZSet中指定分数范围的元素
     * @param key  ZSet的Key
     * @param min 最小分数（包含）
     * @param max 最大分数（包含）
     * @return 删除的元素数量
     */
    Long zRemoveRangeByScore(String key, double min, double max);
    /**
     * 增加分数
     */
    Double zIncrementScore(String key, String value, double delta);

    void sAdd(String key,String value);

    void sAddAll(String key,List<String> values);

    Set<String> sMembers(String key);

    Long sCard(String key);

    boolean sRm(String key,String value);

    void sRm(String key,List<String> values);

    Boolean sIsMember(String key,String value);

}