package com.mall.portal.service.impl;

import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.model.PmsProduct;
import com.mall.portal.cache.BrandCacheService;
import com.mall.portal.domain.enums.SortTypeEnum;
import com.mall.portal.domain.model.ProductCategory;
import com.mall.portal.domain.model.ProductDetail;
import com.mall.portal.service.PortalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PortalProductServiceImpl implements PortalProductService {

    @Autowired private PmsProductMapper productMapper;
    @Autowired private BrandCacheService brandCacheService;


    @Override
    public List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, SortTypeEnum sort) {

    }

    @Override
    public List<ProductCategory> categoryTreeList() {

    }

    @Override
    public ProductDetail detail(Long id) {
        return null;
    }
}
