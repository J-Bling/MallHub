package com.mall.admin.domain.pms;

import com.mall.mbg.model.PmsProductAttribute;
import lombok.Data;

import java.util.List;

@Data //分类关联属性DTO
public class CategoryAttributeRelationDTO {
    private Long categoryId;
    // 已关联的属性列表
    private List<PmsProductAttribute> relatedAttributes;
    // 可选的属性列表
    private List<PmsProductAttribute> optionalAttributes;
}
