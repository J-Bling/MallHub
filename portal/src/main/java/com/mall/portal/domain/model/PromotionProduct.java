package com.mall.portal.domain.model;

import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductFullReduction;
import com.mall.mbg.model.PmsProductLadder;
import com.mall.mbg.model.PmsSkuStock;

import java.util.List;

public class PromotionProduct extends PmsProduct {
    //商品库存信息
    private List<PmsSkuStock> skuStockList;
    //商品打折信息
    private List<PmsProductLadder> productLadderList;
    //商品满减信息
    private List<PmsProductFullReduction> productFullReductionList;

    public List<PmsProductLadder> getProductLadderList() {
        return productLadderList;
    }

    public List<PmsSkuStock> getSkuStockList() {
        return skuStockList;
    }

    public List<PmsProductFullReduction> getProductFullReductionList() {
        return productFullReductionList;
    }

    public void setProductFullReductionList(List<PmsProductFullReduction> productFullReductionList) {
        this.productFullReductionList = productFullReductionList;
    }

    public void setProductLadderList(List<PmsProductLadder> productLadderList) {
        this.productLadderList = productLadderList;
    }

    public void setSkuStockList(List<PmsSkuStock> skuStockList) {
        this.skuStockList = skuStockList;
    }
}
