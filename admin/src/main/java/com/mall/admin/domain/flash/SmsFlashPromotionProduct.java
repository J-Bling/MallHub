package com.mall.admin.domain.flash;

import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.SmsFlashProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


public class SmsFlashPromotionProduct extends SmsFlashProductRelation {
    @Getter
    @Setter
    @ApiModelProperty("关联商品")
    private PmsProduct product;
}
