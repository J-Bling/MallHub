package com.mall.portal.service.impl;

import com.mall.mbg.model.CmsSubject;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductCategory;
import com.mall.portal.domain.model.HomeContent;
import com.mall.portal.service.FlashPromotionService;
import com.mall.portal.service.HomeService;
import com.mall.portal.service.PortalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired private FlashPromotionService flashPromotionService;
    @Autowired private PortalProductService productService;

    @Override
    public HomeContent content() {
        return null;
    }

    @Override
    public List<PmsProduct> recommendProductList(int pageNum, int pageSize) {
        return Collections.emptyList();
    }

    @Override
    public List<PmsProductCategory> getProductCategoryList(long parentId) {
        return productService.categoryTreeList();
    }

    @Override
    public List<CmsSubject> getSubject(long categoryId, int pageNum, int pageSize) {
        return Collections.emptyList();
    }

    @Override
    public List<PmsProduct> getHotProduct(int pageNum, int pageSize) {
        return Collections.emptyList();
    }

    @Override
    public List<PmsProduct> getNewProduct(int pageNum, int pageSize) {
        return Collections.emptyList();
    }
}
