package com.mall.portal.cache.impl;

import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.PmsBrandMapper;
import com.mall.mbg.model.PmsBrand;
import com.mall.portal.cache.BrandCacheService;
import com.mall.portal.dao.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BrandCacheServiceImpl implements BrandCacheService {
    @Autowired private RedisService redisService;
    @Autowired private PmsBrandMapper brandMapper;
    @Autowired private BrandDao brandDao;


    @Override
    public PmsBrand getBrand(long brandId) {
        Object result = redisService.hGet(brandHashKey,HashField(brandId));
        if (defaultNULL.equals(result)){
            return null;
        }
        if (result !=null ){
            return (PmsBrand) result;
        }
        PmsBrand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand!=null){
            redisService.hSet(brandHashKey,HashField(brandId),brand);
        }else {
            redisService.hSet(brandHashKey,HashField(brandId),defaultNULL);
        }
        return brand;
    }

    @Override
    public List<PmsBrand> getBrands(int offset,int limit) {
        Set<Object> sets = redisService.zRange(brandZSetKey,offset,offset+limit-1);
        if (sets==null || sets.isEmpty()){
            List<PmsBrand> brandList = brandDao.findBrands(offset,limit);
            if (brandList==null || brandList.isEmpty()){
                return null;
            }
            Map<String,PmsBrand> pmsBrandMap = new HashMap<>();
            Set<ZSetOperations.TypedTuple<Object>> typedTuples = new HashSet<>();
            for (PmsBrand brand : brandList){
                pmsBrandMap.put(brand.getId()+"",brand);
                typedTuples.add(new DefaultTypedTuple<>(brand.getId()+"",(double)brand.getId()));
            }
            redisService.hSetAll(brandHashKey,pmsBrandMap);
            redisService.zAdd(brandZSetKey,typedTuples);
            return brandList;
        }

        List<String> strings=sets.stream().map(set->(String) set).collect(Collectors.toList());
        List<Object> objectList = redisService.hGetAll(brandHashKey,strings);
        return objectList.stream().map(o->(PmsBrand)o).collect(Collectors.toList());
    }

    @Override
    public void delBrandCache(long brandId) {
        redisService.zRemove(brandZSetKey,brandId+"");
        redisService.hDel(brandHashKey,HashField(brandId));
    }
}
