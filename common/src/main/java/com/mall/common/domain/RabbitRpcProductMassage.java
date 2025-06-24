package com.mall.common.domain;

public class RabbitRpcProductMassage extends RabbitRpcMessage{
    private Long productId;
    private Long skuId;

    public RabbitRpcProductMassage(){}

    public static RabbitRpcProductMassage Builder(){
        return new RabbitRpcProductMassage();
    }

    public RabbitRpcProductMassage addSkuId(Long skuId){
        this.skuId=skuId;
        return this;
    }

    public RabbitRpcProductMassage addProductId(Long productId){
        this.productId = productId;
        return this;
    }

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
