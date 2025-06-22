package com.mall.admin.domain.pms;

import com.mall.mbg.model.PmsProduct;
import lombok.Data;

import java.util.Date;

@Data//商品列表项响应DTO
public class ProductListItemDTO {
    // 商品基础信息
    private PmsProduct product;

    // 品牌名称
    private String brandName;

    // 分类名称
    private String categoryName;

    // 是否在促销中
    private Boolean onPromotion;

    // 当前时间（用于前端判断促销状态）
    private Date currentTime;
}
