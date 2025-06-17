package com.mall.portal.domain.model;

import com.mall.mbg.model.PmsProductCategory;
import java.util.List;

public class ProductCategory extends PmsProductCategory {
    private List<ProductCategory> productCategoryList;

    public static ProductCategory getInstance(PmsProductCategory category){
        if (category == null) {
            return null;
        }
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(category.getId());
        productCategory.setParentId(category.getParentId());
        productCategory.setName(category.getName());
        productCategory.setLevel(category.getLevel());
        productCategory.setProductCount(category.getProductCount());
        productCategory.setProductUnit(category.getProductUnit());
        productCategory.setNavStatus(category.getNavStatus());
        productCategory.setShowStatus(category.getShowStatus());
        productCategory.setSort(category.getSort());
        productCategory.setIcon(category.getIcon());
        productCategory.setKeywords(category.getKeywords());
        productCategory.setDescription(category.getDescription());

        return productCategory;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}
