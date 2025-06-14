package com.mall.portal.domain.model;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class OrderReturnApplyParam {
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("退货商品id")
    private Long productId;
    @ApiModelProperty("订单编号")
    private String orderSn;
    @ApiModelProperty("会员用户名")
    private String memberUsername;
    @ApiModelProperty("退货人姓名")
    private String returnName;
    @ApiModelProperty("退货人电话")
    private String returnPhone;
    @ApiModelProperty("商品图片")
    private String productPic;
    @ApiModelProperty("商品名称")
    private String productName;
    @ApiModelProperty("商品品牌")
    private String productBrand;
    @ApiModelProperty("商品销售属性：颜色：红色；尺码：xl;")
    private String productAttr;
    @ApiModelProperty("退货数量")
    private Integer productCount;
    @ApiModelProperty("商品单价")
    private BigDecimal productPrice;
    @ApiModelProperty("商品实际支付单价")
    private BigDecimal productRealPrice;
    @ApiModelProperty("原因")
    private String reason;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("凭证图片，以逗号隔开")
    private String proofPics;

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPic() {
        return productPic;
    }

    public String getDescription() {
        return description;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public String getProductAttr() {
        return productAttr;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public String getProofPics() {
        return proofPics;
    }

    public String getReason() {
        return reason;
    }

    public String getReturnName() {
        return returnName;
    }

    public String getReturnPhone() {
        return returnPhone;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public BigDecimal getProductRealPrice() {
        return productRealPrice;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public void setProductRealPrice(BigDecimal productRealPrice) {
        this.productRealPrice = productRealPrice;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    public void setProofPics(String proofPics) {
        this.proofPics = proofPics;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setReturnPhone(String returnPhone) {
        this.returnPhone = returnPhone;
    }
}
