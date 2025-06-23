package com.mall.admin.domain.pms;

import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsProductAttributeCategory;
import com.mall.mbg.model.PmsProductAttributeValue;
import lombok.Data;

import java.util.List;

@Data
public class ProductAttributeDetail {
    private PmsProductAttribute attribute;
    private PmsProductAttributeCategory attributeCategory;
    private List<PmsProductAttributeValue> attributeValueList;
}
