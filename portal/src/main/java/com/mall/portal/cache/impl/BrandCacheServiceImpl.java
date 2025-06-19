package com.mall.portal.cache.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.PmsBrandMapper;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsBrandExample;
import com.mall.portal.cache.BrandCacheService;
import com.mall.portal.dao.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BrandCacheServiceImpl implements BrandCacheService {
    @Autowired private RedisService redisService;
    @Autowired private PmsBrandMapper brandMapper;
    @Autowired private BrandDao brandDao;
    @Autowired private CounterRedisService counterRedisService;

    private int brandSize = 0;

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
        if (brandSize>0 && brandSize<=offset){
            return null;
        }
        Set<String> sets = counterRedisService.zRange(brandZSetKey,offset,offset+limit-1);
        if (sets==null || sets.isEmpty()){
            List<PmsBrand> brandList = brandMapper.selectByExample(new PmsBrandExample());
            if (brandList==null || brandList.isEmpty()){
                return null;
            }
            brandSize = brandList.size();
            Map<String,PmsBrand> pmsBrandMap = new HashMap<>();
            Map<String,Double> zSetMap = new HashMap<>();
            for (PmsBrand brand : brandList){
                pmsBrandMap.put(brand.getId()+"",brand);
                zSetMap.put(brand.getId()+"",(double) brand.getId());
            }
            redisService.hSetAll(brandHashKey,pmsBrandMap);
            counterRedisService.zAddAll(brandZSetKey,zSetMap);
            return brandList;
        }

        List<String> strings= new ArrayList<>(sets);
        List<Object> objectList = redisService.hGetAll(brandHashKey,strings);
        return objectList.stream().map(o->(PmsBrand)o).collect(Collectors.toList());
    }

    @Override
    public void delBrandCache(long brandId) {
        counterRedisService.zRemove(brandZSetKey,brandId+"");
        redisService.hDel(brandHashKey,HashField(brandId));
        brandSize = 0;
    }
}
