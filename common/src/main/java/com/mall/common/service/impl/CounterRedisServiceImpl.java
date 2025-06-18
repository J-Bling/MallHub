package com.mall.common.service.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CounterRedisServiceImpl implements CounterRedisService {
    @Autowired private HashOperations<String,String,String> hashOperations;
    @Autowired private RedisTemplate<String,String> stringRedisTemplate;
    @Autowired private RedisService redisService;


    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return hashOperations.hasKey(key,hashKey);
    }

    @Override
    public Long hInCr(String key, String hashKey, long delta) {
        Long len = hashOperations.increment(key,hashKey,delta);
        redisService.tryExpire(key);
        return len;
    }

    @Override
    public Long hDeCr(String key, String hashKey, long delta) {
        return hashOperations.increment(key,hashKey,-delta);
    }

    @Override
    public String hGet(String key, String field) {
        return hashOperations.get(key,field);
    }

    @Override
    public void hSet(String key, String field, String value) {
        hashOperations.put(key,field,value);
        redisService.tryExpire(key);
    }

    @Override
    public void hSet(String key, String field, String value, long expired) {
        this.hSet(key,field,value);
        redisService.tryExpire(key);
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public void hSetAll(String key, Map<String, String> fieldValues) {
        hashOperations.putAll(key,fieldValues);
        redisService.tryExpire(key);
    }

    @Override
    public void hSetAll(String key, Map<String, String> fieldValues, long expired) {
        this.hSetAll(key,fieldValues);
        redisService.expire(key,expired);
    }

    @Override
    public void hDel(String key, String... fields) {
        hashOperations.delete(key, (Object) fields);
    }

    @Override
    public Boolean del(String key) {
        return redisService.del(key);
    }

    @Override
    public Long del(List<String> keys) {
        return redisService.del(keys);
    }




    @Override
    public Boolean zAdd(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key,value,score);
    }

    @Override
    public Long zAddAll(String key, Map<String, Double> values) {
        if (values==null||values.isEmpty()){
            return 0L;
        }
        return stringRedisTemplate.opsForZSet().add(key,values.entrySet()
                .stream()
                .map(e->new DefaultTypedTuple<>(e.getKey(),e.getValue()))
                .collect(Collectors.toSet())
        );
    }

    @Override
    public Long zRemove(String key, String... values) {
        return stringRedisTemplate.opsForZSet().remove(key,(Object[]) values);
    }

    @Override
    public Double zScore(String key, String value) {
        return stringRedisTemplate.opsForZSet().score(key,value);
    }

    @Override
    public Long zRank(String key, String value) {
        return stringRedisTemplate.opsForZSet().rank(key,value);
    }

    @Override
    public Long zReverseRank(String key, String value) {
        return stringRedisTemplate.opsForZSet().reverseRank(key,value);
    }

    @Override
    public Set<String> zRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key,min,max);
    }

    @Override
    public Set<String> zRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key,start,end);
    }

    @Override
    public Set<String> zReverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key,start,end);
    }

    @Override
    public Long zSize(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    @Override
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().removeRangeByScore(key,min,max);
    }

    @Override
    public Double incrementScore(String key, String value, double delta) {
        return stringRedisTemplate.opsForZSet().incrementScore(key,value,delta);
    }
}
