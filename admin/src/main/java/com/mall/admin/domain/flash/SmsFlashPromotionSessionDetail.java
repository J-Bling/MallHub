package com.mall.admin.domain.flash;

import com.mall.mbg.model.SmsFlashSession;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


public class SmsFlashPromotionSessionDetail extends SmsFlashSession {
    @Setter
    @Getter
    @ApiModelProperty("商品数量")
    private Long productCount;
}
