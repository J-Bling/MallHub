package com.mall.admin.domain.pms;

import com.mall.mbg.model.PmsProductCategory;
import lombok.Data;

import java.util.List;

@Data//商品分类DTO
public class ProductCategoryTreeNodeDTO {
    private PmsProductCategory category;
    private List<ProductCategoryTreeNodeDTO> children;
}
