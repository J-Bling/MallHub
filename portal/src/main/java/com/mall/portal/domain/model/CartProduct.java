package com.mall.portal.domain.model;

import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class CartProduct extends PmsProduct {
    @ApiModelProperty("商品属性列表")
    private List<PmsProductAttribute> productAttributeList;
    @ApiModelProperty("商品SKU库存列表")
    private List<PmsSkuStock> pmsSkuStockList;

    public List<PmsSkuStock> getPmsSkuStockList() {
        return pmsSkuStockList;
    }

    public List<PmsProductAttribute> getProductAttributeList() {
        return productAttributeList;
    }

    public void setProductAttributeList(List<PmsProductAttribute> productAttributeList) {
        this.productAttributeList = productAttributeList;
    }

    public void setPmsSkuStockList(List<PmsSkuStock> pmsSkuStockList) {
        this.pmsSkuStockList = pmsSkuStockList;
    }
}
