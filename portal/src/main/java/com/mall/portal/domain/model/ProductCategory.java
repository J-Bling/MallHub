package com.mall.portal.domain.model;

import com.mall.mbg.model.PmsProductCategory;
import java.util.List;

public class ProductCategory extends PmsProductCategory {
    //子分类集合
    private List<ProductCategory> productCategoryList;

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}
