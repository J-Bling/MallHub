package com.mall.admin.domain.pms;

import com.mall.mbg.model.*;
import lombok.Data;

import java.util.List;

@Data
//商品详情响应DTO
public class ProductDetailDTO {
    // 商品基础信息
    private PmsProduct product;

    // 商品分类（带父级分类名称）
    private PmsProductCategory category;
    private String parentCategoryName;

    // 商品品牌信息
    private PmsBrand brand;

    // 商品属性及相关值
    private List<ProductAttrInfo> productAttrInfoList;

    // SKU库存信息
    private List<PmsSkuStock> skuStockList;

    // 商品图片列表
    private List<PmsAlbumPic> albumPics;

    // 静态类：商品属性信息
    public static class ProductAttrInfo {
        private PmsProductAttribute productAttribute;
        private List<PmsProductAttributeValue> attributeValues;

        public PmsProductAttribute getProductAttribute() {
            return productAttribute;
        }

        public List<PmsProductAttributeValue> getAttributeValues() {
            return attributeValues;
        }

        public void setAttributeValues(List<PmsProductAttributeValue> attributeValues) {
            this.attributeValues = attributeValues;
        }

        public void setProductAttribute(PmsProductAttribute productAttribute) {
            this.productAttribute = productAttribute;
        }
    }
}
