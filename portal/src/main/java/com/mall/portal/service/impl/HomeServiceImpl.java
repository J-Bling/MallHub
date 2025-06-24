package com.mall.portal.service.impl;

import com.mall.common.constant.enums.flash.FlashPromotionTypeEnum;
import com.mall.mbg.model.*;
import com.mall.portal.dao.HomeDao;
import com.mall.portal.domain.model.HomeContent;
import com.mall.portal.service.FlashPromotionService;
import com.mall.portal.service.HomeService;
import com.mall.portal.service.BrandService;
import com.mall.portal.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired private FlashPromotionService flashPromotionService;
    @Autowired private ProductService productService;
    @Autowired private BrandService brandService;
    @Autowired private HomeDao homeDao;


    @Override
    public HomeContent content() {
        //首页内容
        HomeContent homeContent = new HomeContent();
        homeContent.setBrandList(brandService.recommendBrands(10));
        homeContent.setHotProductList(this.getHotProduct(0,6));
        homeContent.setNewProductList(this.getNewProduct(0,6));
        homeContent.setSubjectList(homeDao.recommendSubjects(0,5));
        homeContent.setAdvertiseList(homeDao.recommendHomeAdvertise(new Date(),0,5));
        homeContent.setFlashPromotionList(flashPromotionService.getStartFlashPromotion(FlashPromotionTypeEnum.PLATFORM_ACTIVITY.getCode()));
        return homeContent;
    }

    @Override
    public List<PmsProduct> recommendProductList(int offset, int limit) {
        return productService.recommendProducts(offset,limit,1);
    }

    @Override
    public List<PmsProductCategory> getProductCategoryList(long parentId) {
        return productService.categoryTreeList();
    }

    @Override
    public List<CmsSubject> getSubject(long categoryId, int offset, int limit) {
        return homeDao.recommendSubjectsByCategory(categoryId,offset,limit);
    }

    @Override
    public List<PmsProduct> getHotProduct(int offset, int limit) {
        return productService.recommendProducts(offset,limit,2);
    }

    @Override
    public List<PmsProduct> getNewProduct(int offset, int limit) {
        return productService.recommendProducts(offset,limit,3);
    }
}
