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
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    @Override
    public Long inCr(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key,delta);
    }

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
    }

    @Override
    public void hSetAll(String key, Map<String, String> filedValues, int noExpired) {
        hashOperations.putAll(key,filedValues);
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
    public void zAdd(String key,String value,double score,int noExpired){
        stringRedisTemplate.opsForZSet().add(key,value,score);
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
    public void zAddAll(String key, Map<String, Double> value, int noExpired) {
        stringRedisTemplate.opsForZSet().add(key,value.entrySet()
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
    public Double zIncrementScore(String key, String value, double delta) {
        return stringRedisTemplate.opsForZSet().incrementScore(key,value,delta);
    }

    @Override
    public void sAdd(String key, String value) {
        stringRedisTemplate.opsForSet().add(key,value);
    }

    @Override
    public void sAddAll(String key, List<String> values) {
        String[] strings = new String[values.size()];
        for (int i=0;i<values.size();i++){
            strings[i] = values.get(i);
        }
        stringRedisTemplate.opsForSet().add(key,strings);
    }

    @Override
    public Set<String> sMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    @Override
    public Long sCard(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    @Override
    public boolean sRm(String key, String value) {
        Long len = stringRedisTemplate.opsForSet().remove(key,value);
        return len !=null && len >0;
    }

    @Override
    public void sRm(String key, List<String> values) {
        Object[] objects = new Object[values.size()];
        for (int i=0;i<values.size();i++){
            objects[i] = values.get(i);
        }
        stringRedisTemplate.opsForSet().remove(key,objects);
    }

    @Override
    public Boolean sIsMember(String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key,value);
    }
}
