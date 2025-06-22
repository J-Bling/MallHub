package com.mall.common.domain;

public class RabbitRpcProductMassage extends RabbitRpcMessage{
    private Long productId;
    private Long skuId;

    public Long getSkuId() {
        return skuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
