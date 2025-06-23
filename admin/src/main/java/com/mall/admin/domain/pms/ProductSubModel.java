package com.mall.admin.domain.pms;

import com.mall.mbg.model.PmsProductAttributeValue;
import com.mall.mbg.model.PmsProductFullReduction;
import com.mall.mbg.model.PmsProductLadder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductSubModel implements Serializable {
    private Long productId;
    private List<PmsProductLadder> productLadderList;
    private List<PmsProductFullReduction> productFullReductionList;
    private ProductAlbums productAlbums;
    private List<PmsProductAttributeValue> productAttributeValueList;
}
