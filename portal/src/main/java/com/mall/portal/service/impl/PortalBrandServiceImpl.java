package com.mall.portal.service.impl;

import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsProduct;
import com.mall.portal.cache.BrandCacheService;
import com.mall.portal.dao.ProductDao;
import com.mall.portal.service.PortalBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortalBrandServiceImpl implements PortalBrandService {
    @Autowired private BrandCacheService brandCacheService;
    @Autowired private ProductDao productDao;


    @Override
    public List<PmsBrand> brandList(int offset, int limit) {
        return brandCacheService.getBrands(offset,limit);
    }

    @Override
    public PmsBrand detail(long brandId) {
        return brandCacheService.getBrand(brandId);
    }

    @Override
    public List<PmsProduct> productPage(long brandId, int offset,int limit) {
        return productDao.getProductByBrandId(brandId,offset,limit);
    }
}
