package com.mall.common.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RabbitCouponMessage  extends RabbitRpcMessage{
    private Long couponId;
    private Boolean isAllUseType;
    private Long productId;
    private Long productCateId;

    public RabbitCouponMessage(){}
}
