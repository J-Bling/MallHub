package com.mall.portal.domain.model;

import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsProductAttributeValue;
import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.PmsProductFullReduction;
import com.mall.mbg.model.PmsProductLadder;
import com.mall.mbg.model.PmsSkuStock;

import java.io.Serializable;
import java.util.List;

public class ProductDetail implements Serializable {
    //"商品信息"
    private PmsProduct product;
    //"商品属性与参数"
    private List<PmsProductAttribute> productAttributeList;
    //"手动录入的商品属性与参数值"
    private List<PmsProductAttributeValue> productAttributeValueList;
    //"商品的sku库存信息"
    private List<PmsSkuStock> skuStockList;
    //"商品阶梯价格设置"
    private List<PmsProductLadder> productLadderList;
    //"商品满减价格设置"
    private List<PmsProductFullReduction> productFullReductionList;
    //商品相册集
    private ProductAlbums productAlbums;
    //"商品品牌"
    private PmsBrand brand;
    //"商品可用优惠券"
    private List<SmsCoupon> couponList;

    public void setProductFullReductionList(List<PmsProductFullReduction> productFullReductionList) {
        this.productFullReductionList = productFullReductionList;
    }

    public PmsBrand getBrand() {
        return brand;
    }

    public PmsProduct getProduct() {
        return product;
    }

    public List<SmsCoupon> getCouponList() {
        return couponList;
    }

    public List<PmsSkuStock> getSkuStockList() {
        return skuStockList;
    }

    public List<PmsProductLadder> getProductLadderList() {
        return productLadderList;
    }

    public List<PmsProductAttribute> getProductAttributeList() {
        return productAttributeList;
    }

    public List<PmsProductFullReduction> getProductFullReductionList() {
        return productFullReductionList;
    }

    public List<PmsProductAttributeValue> getProductAttributeValueList() {
        return productAttributeValueList;
    }

    public ProductAlbums getProductAlbums() {
        return productAlbums;
    }

    public void setSkuStockList(List<PmsSkuStock> skuStockList) {
        this.skuStockList = skuStockList;
    }

    public void setProductLadderList(List<PmsProductLadder> productLadderList) {
        this.productLadderList = productLadderList;
    }

    public void setProductAttributeList(List<PmsProductAttribute> productAttributeList) {
        this.productAttributeList = productAttributeList;
    }

    public void setProduct(PmsProduct product) {
        this.product = product;
    }

    public void setBrand(PmsBrand brand) {
        this.brand = brand;
    }

    public void setCouponList(List<SmsCoupon> couponList) {
        this.couponList = couponList;
    }

    public void setProductAttributeValueList(List<PmsProductAttributeValue> productAttributeValueList) {
        this.productAttributeValueList = productAttributeValueList;
    }

    public void setProductAlbums(ProductAlbums productAlbums) {
        this.productAlbums = productAlbums;
    }
}
