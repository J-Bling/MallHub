package com.mall.admin.cache.impl;

import com.mall.admin.cache.BrandCacheManage;
import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.model.PmsBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BrandCacheManageImpl implements BrandCacheManage {
    @Autowired
    private RedisService redisService;

    @Autowired
    private CounterRedisService counterRedisService;

    @Value("${redis.key.brandZSetKey:brand-zSet-key}")
    private String brandZSetKey;

    @Value("${redis.key.brandHashKey:brand-hash-key}")
    private String brandHashKey;


    @Override
    public void delBrandCache(Long id) {
        counterRedisService.zRemove(brandZSetKey,""+id);
        redisService.hDel(brandHashKey,""+id);
    }

    @Override
    public void add(PmsBrand brand) {
        if (brand==null || brand.getId()==null){
            return;
        }
        counterRedisService.zAdd(brandZSetKey,""+brand.getId(),brand.getId());
        redisService.hSet(brandHashKey,""+brand.getId(),brand);
    }
}
