package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.PmsBrand;

import java.util.List;

public interface BrandCacheService extends Cache {
    PmsBrand getBrand(long brandId);
    List<PmsBrand> getBrands(int pageNum,int pageSize);
    default String HashField(long brandId){return ""+brandId;}
}