package com.mall.portal.service.impl;

import com.mall.mbg.mapper.*;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductCategory;
import com.mall.mbg.model.PmsProductExample;
import com.mall.portal.cache.ProductCategoryCacheService;
import com.mall.portal.domain.enums.SortTypeEnum;
import com.mall.portal.domain.model.ProductDetail;
import com.mall.portal.service.PortalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortalProductServiceImpl implements PortalProductService {

    @Autowired private PmsProductMapper productMapper;
    @Autowired private ProductCategoryCacheService categoryCacheService;


    @Override
    public List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, SortTypeEnum sort) {
        if(pageNum == null || pageNum < 0) pageNum = 0;
        if(pageSize == null || pageSize < 5 || pageSize > 30) pageSize = 6;
        PmsProductExample example = new PmsProductExample();
        PmsProductExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        criteria.andPublishStatusEqualTo(1);
        if (keyword!=null && !keyword.isEmpty()){
            criteria.andNameLike("%"+keyword+"%");
        }
        if (brandId!=null && brandId >0){
            criteria.andBrandIdEqualTo(brandId);
        }
        if (productCategoryId!=null && productCategoryId>0){
            criteria.andProductCategoryIdEqualTo(productCategoryId);
        }
        example.setOrderByClause(sort.getOrderByClause()+" limit "+pageNum*pageSize+" ,"+pageSize);
        return productMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductCategory> categoryTreeList() {
        return this.categoryCacheService.getAll();
    }


    @Override
    public ProductDetail detail(Long id) {
        return null;
    }
}
