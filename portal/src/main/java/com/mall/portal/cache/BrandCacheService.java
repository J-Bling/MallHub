package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.PmsBrand;

import java.util.List;

public interface BrandCacheService extends Cache {
    PmsBrand getBrand(long brandId);
    List<PmsBrand> getBrands(int pageNum,int pageSize);
    void delBrandCache(long brandId);

    String brandHashKey = "brand-hash-key";
    String brandZSetKey = "brand-zSet-key";
    default String HashField(long brandId){return ""+brandId;}
}
