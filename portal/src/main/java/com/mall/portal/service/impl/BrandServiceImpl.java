package com.mall.portal.service.impl;

import com.mall.mbg.mapper.PmsBrandMapper;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsBrandExample;
import com.mall.mbg.model.PmsProduct;
import com.mall.portal.cache.BrandCacheService;
import com.mall.portal.dao.ProductDao;
import com.mall.portal.domain.model.product.AttentionBrand;
import com.mall.portal.service.AttentionBrandService;
import com.mall.portal.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired private BrandCacheService brandCacheService;
    @Autowired private ProductDao productDao;
    @Autowired private AttentionBrandService attentionBrandService;
    @Autowired private PmsBrandMapper brandMapper;


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

    @Override
    public List<PmsBrand> recommendBrands(int limit) {
        List<PmsBrand> brandList = new ArrayList<>();
        List<AttentionBrand> attentionBrandList = attentionBrandService.list(0,limit);
        if (attentionBrandList!=null && !attentionBrandList.isEmpty()){
            List<Long> brandIds = new ArrayList<>();
            for (AttentionBrand brand : attentionBrandList){
                brandIds.add(brand.getBrandId());
                brandList.add(brandMapper.selectByPrimaryKey(brand.getBrandId()));
            }
            PmsBrandExample example = new PmsBrandExample();
            example.createCriteria().andIdNotIn(brandIds).andShowStatusEqualTo(1);
            example.setOrderByClause("sort desc limit "+(limit-brandIds.size()));
            List<PmsBrand> pmsBrandList = brandMapper.selectByExample(example);
            if (pmsBrandList!=null && !pmsBrandList.isEmpty()){
                brandList.addAll(pmsBrandList);
            }
            return brandList;
        }else {
            PmsBrandExample example = new PmsBrandExample();
            example.createCriteria().andShowStatusEqualTo(1);
            example.setOrderByClause("sort desc limit "+limit);
            brandList = brandMapper.selectByExample(example);
        }
        return brandList;
    }
}
