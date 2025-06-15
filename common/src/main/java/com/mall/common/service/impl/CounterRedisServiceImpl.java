package com.mall.common.service.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import java.util.List;
import java.util.Map;

public class CounterRedisServiceImpl implements CounterRedisService {
    @Autowired private HashOperations<String,String,String> hashOperations;
    @Autowired private RedisService redisService;


    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return hashOperations.hasKey(key,hashKey);
    }

    @Override
    public Long hInCr(String key, String hashKey, long delta) {
        return hashOperations.increment(key,hashKey,delta);
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
        redisService.expire(key,expired);
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
}
