package com.mall.admin.domain.pms;

import com.mall.mbg.model.*;
import lombok.Data;

import java.util.List;

@Data
//商品创建/更新请求DTO
public class ProductCreateOrUpdateDTO {
    // 基础信息
    private PmsProduct product;

    // 商品属性值列表
    private List<PmsProductAttributeValue> productAttributeValueList;

    // SKU库存信息
    private List<PmsSkuStock> skuStockList;

    // 阶梯价格设置
    private List<PmsProductLadder> productLadderList;

    // 满减价格设置
    private List<PmsProductFullReduction> productFullReductionList;

    // 会员价格设置
    private List<PmsMemberPrice> memberPriceList;

    // 商品相册图片ID列表
    private List<Long> albumPicIds;
}
