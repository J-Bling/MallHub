package com.mall.portal.cache.impl;

import com.mall.common.service.CounterRedisService;
import com.mall.common.service.RedisService;
import com.mall.mbg.mapper.PmsBrandMapper;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsBrandExample;
import com.mall.portal.cache.BrandCacheService;
import com.mall.portal.dao.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BrandCacheServiceImpl implements BrandCacheService {
    @Autowired private RedisService redisService;
    @Autowired private PmsBrandMapper brandMapper;
    @Autowired private BrandDao brandDao;
    @Autowired private CounterRedisService counterRedisService;

    @Value("${redis.key.brandHashKey:brand-hash-key}")
    private String brandHashKey;

    @Override
    public void addBrand(long brandId) {
        PmsBrand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand!=null){
            redisService.hSet(brandHashKey,""+brandId,brand);
        }
    }

    @Override
    public void addAll(List<Long> ids) {
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        List<PmsBrand> brandList = brandMapper.selectByExample(example);
        if (!brandList.isEmpty()){
            Map<String,PmsBrand> brandMap = new HashMap<>();
            for (PmsBrand brand : brandList){
                brandMap.put(""+brand.getId(),brand);
            }
            redisService.hSetAll(brandHashKey,brandMap);
        }
    }

    @Override
    public void delBrand(long brandId) {
        redisService.hDel(brandHashKey,""+brandId);
    }

    @Override
    public void updateBrand(long brandId) {
        this.addBrand(brandId);
    }

    @Override
    public void cleanBrand() {
        redisService.del(brandHashKey);
    }

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
    public List<PmsBrand> getBrands() {
        List<PmsBrand> brandList = new ArrayList<>();
        Map<Object,Object> map = redisService.hGetAll(brandHashKey);
        if (map==null || map.isEmpty()){
            brandList = brandMapper.selectByExample(new PmsBrandExample());
            if (!brandList.isEmpty()){
                Map<String,PmsBrand> brandMap = new HashMap<>();
                for (PmsBrand brand : brandList){
                    brandMap.put(""+brand.getId(),brand);
                }
                redisService.hSetAll(brandHashKey,brandMap);
            }
        }
        return brandList;
    }
}
