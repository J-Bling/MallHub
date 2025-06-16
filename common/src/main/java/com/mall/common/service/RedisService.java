package com.mall.common.service;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作Service
 */
public interface RedisService {
    TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    /**
     * 保存属性
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性
     */
    void set(String key, Object value);

    Boolean setNX(String key,String value,long time);

    /**
     * 获取属性
     */
    Object get(String key);

    /**
     * 删除属性
     */
    Boolean del(String key);

    /**
     * 批量删除属性
     */
    Long del(List<String> keys);

    /**
     * 设置过期时间
     */
    Boolean expire(String key, long time);
    /**
     * 尝试设置过期时间
     */
    void tryExpire(String key);

    /**
     * 获取过期时间
     */
    Long getExpire(String key);

    /**
     * 判断是否有该属性
     */
    Boolean hasKey(String key);

    /**
     * 按delta递增
     */
    Long incr(String key, long delta);

    /**
     * 按delta递减
     */
    Long decr(String key, long delta);

    /**
     * 获取Hash结构中的属性
     */
    Object hGet(String key, String hashKey);

    /**
     * 向Hash结构中放入一个属性
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个属性
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 直接获取整个Hash结构
     */
    Map<Object, Object> hGetAll(String key);
    /**
     * 获取多个字段值
     */
    List<Object> hGetAll(String key,List<String> ids);

    /**
     * 直接设置整个Hash结构
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 直接设置整个Hash结构
     */
    void hSetAll(String key, Map<String, ?> map);

    /**
     * 删除Hash结构中的属性
     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断Hash结构中是否有该属性
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Hash结构中属性递增
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash结构中属性递减
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 获取Set结构
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set结构中添加属性
     */
    Long sAdd(String key, Object... values);

    /**
     * 向Set结构中添加属性
     */
    Long sAdd(String key, long time, Object... values);

    /**
     * 是否为Set中的属性
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set结构的长度
     */
    Long sSize(String key);

    /**
     * 删除Set结构中的属性
     */
    Long sRemove(String key, Object... values);

    /**
     * 获取List结构中的属性
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List结构的长度
     */
    Long lSize(String key);

    /**
     * 根据索引获取List中的属性
     */
    Object lIndex(String key, long index);

    /**
     * 向List结构中添加属性
     */
    Long lPush(String key, Object value);

    /**
     * 向List结构中添加属性
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List结构中批量添加属性
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List结构中批量添加属性
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从List结构中移除属性
     */
    Long lRemove(String key, long count, Object value);
    /**
     * 添加元素到ZSet
     * @param key   键
     * @param value 成员
     * @param score 分数
     * @return 是否成功
     */
    Boolean zAdd(String key, Object value, double score);

    /**
     * 批量添加元素到ZSet
     * @param key    键
     * @param tuples 成员-分数集合
     * @return 添加成功的数量
     */
    Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples);

    /**
     * 移除ZSet中的元素
     * @param key    键
     * @param values 成员数组
     * @return 移除的数量
     */
    Long zRemove(String key, Object... values);

    /**
     * 增加元素的分数
     * @param key   键
     * @param value 成员
     * @param delta 增量
     * @return 增加后的分数
     */
    Double zIncrementScore(String key, Object value, double delta);

    /**
     * 获取元素的分数
     * @param key   键
     * @param value 成员
     * @return 分数（不存在返回null）
     */
    Double zScore(String key, Object value);

    /**
     * 获取元素在ZSet中的排名（升序，从0开始）
     * @param key   键
     * @param value 成员
     * @return 排名（不存在返回null）
     */
    Long zRank(String key, Object value);

    /**
     * 获取元素在ZSet中的排名（降序，从0开始）
     * @param key   键
     * @param value 成员
     * @return 排名（不存在返回null）
     */
    Long zReverseRank(String key, Object value);

    /**
     * 获取ZSet中指定排名范围的成员（升序）
     * @param key   键
     * @param start 开始排名（包含）
     * @param end   结束排名（包含）
     * @return 成员集合
     */
    Set<Object> zRange(String key, long start, long end);

    /**
     * 获取ZSet中指定排名范围的成员（降序）
     * @param key   键
     * @param start 开始排名（包含）
     * @param end   结束排名（包含）
     * @return 成员集合
     */
    Set<Object> zReverseRange(String key, long start, long end);

    /**
     * 获取ZSet中指定分数范围的成员（升序）
     * @param key 键
     * @param min 最小分数（包含）
     * @param max 最大分数（包含）
     * @return 成员集合
     */
    Set<Object> zRangeByScore(String key, double min, double max);

    /**
     * 获取ZSet中指定分数范围的成员（带分数，升序）
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 成员-分数集合
     */
    Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key, double min, double max);

    /**
     * 获取ZSet中成员数量
     * @param key 键
     * @return 成员总数
     */
    Long zCard(String key);

    /**
     * 统计ZSet中分数在[min,max]之间的成员数量
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 成员数量
     */
    Long zCount(String key, double min, double max);

    /**
     * 移除ZSet中排名在[start,end]之间的成员
     * @param key   键
     * @param start 开始排名
     * @param end   结束排名
     * @return 移除的数量
     */
    Long zRemoveRange(String key, long start, long end);

    /**
     * 移除ZSet中分数在[min,max]之间的成员
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 移除的数量
     */
    Long zRemoveRangeByScore(String key, double min, double max);
}