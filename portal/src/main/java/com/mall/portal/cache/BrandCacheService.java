package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.PmsBrand;

import java.util.List;

public interface BrandCacheService extends Cache {
    void addBrand(long brandId);
    void addAll(List<Long> ids);
    void delBrand(long brandId);
    void updateBrand(long brandId);
    void cleanBrand();
    PmsBrand getBrand(long brandId);
    List<PmsBrand> getBrands();
    default String HashField(long brandId){return ""+brandId;}
}