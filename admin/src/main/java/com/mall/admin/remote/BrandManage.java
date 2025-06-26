package com.mall.admin.remote;

public interface BrandManage extends Manage {
    void addBrand(Long brandId);

    void addAllBrands();

    void deleteBrand(Long brandId);

    void updateBrand(Long brandId);

    void cleanBrandCache();
}
