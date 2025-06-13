package com.mall.portal.cache.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.service.RedisService;
import com.mall.mbg.model.UmsMember;
import com.mall.portal.cache.ConsumerCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerCacheServiceImpl implements ConsumerCacheService {
    @Autowired private RedisService redisService;
    private final Logger logger = LoggerFactory.getLogger(ConsumerCacheServiceImpl.class);

    @Override
    public void setCode(String phone, String code) {
        String key = ConsumerCacheService.CodeKey+phone;
        redisService.set(key,code,ConsumerCacheService.CodeExpired);
    }

    @Override
    public String getCode(String phone) {
        String key = ConsumerCacheService.CodeKey+phone;
        try{
            return (String) redisService.get(key);
        }catch (Exception e){
            logger.error("反序列化发送异常 : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public void delCode(String phone) {
        String key = ConsumerCacheService.CodeKey+phone;
        this.del(key);
    }

    private void del(String key) {
        redisService.del(key);
    }

    @Override
    public void setMember(UmsMember member) {
        try {
            String key = ConsumerCacheService.MemberKey + member.getId();
            redisService.set(key,member);
        }catch (Exception e){
            logger.error("序列化/设置缓存 member对象失败 原因:{}",e.getMessage());
        }
    }

    @Override
    public void setMember(Long id, String NULL) {
        String key = ConsumerCacheService.MemberKey + id;
        redisService.set(key,NULL);
    }

    @Override
    public UmsMember getMember(Long id) {
        try {
            String key = ConsumerCacheService.MemberKey + id;
            Object value = redisService.get(key);
            if (value==null){
                return null;
            }
            if (ConsumerCacheService.defaultNULL.equals(value)){
                return new UmsMember();
            }
            return (UmsMember) value;
        }catch (Exception e){
            logger.error("member 反序列化失败 {}",e.getMessage());
            return null;
        }
    }

    @Override
    public void delMember(Long id) {
        String key = ConsumerCacheService.MemberKey + id;
        this.del(key);
    }

    @Override
    public void unLock(String key) {
        this.del(key);
    }

    @Override
    public boolean tryLock(String key) {
        Boolean isLocked = redisService.setNX(key,ConsumerCacheService.lockValue,ConsumerCacheService.defaultLockExpired);
        return isLocked != null && isLocked;
    }
}
