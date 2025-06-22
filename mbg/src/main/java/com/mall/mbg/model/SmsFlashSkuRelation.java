package com.mall.mbg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SmsFlashSkuRelation implements Serializable {
    private Long id;

    private Long productRelationId;

    private Long productId;

    private Long skuId;

    private String skuCode;

    private BigDecimal flashPrice;

    private Integer flashStock;

    private Integer flashLimit;

    private BigDecimal originalPrice;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductRelationId() {
        return productRelationId;
    }

    public void setProductRelationId(Long productRelationId) {
        this.productRelationId = productRelationId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public BigDecimal getFlashPrice() {
        return flashPrice;
    }

    public void setFlashPrice(BigDecimal flashPrice) {
        this.flashPrice = flashPrice;
    }

    public Integer getFlashStock() {
        return flashStock;
    }

    public void setFlashStock(Integer flashStock) {
        this.flashStock = flashStock;
    }

    public Integer getFlashLimit() {
        return flashLimit;
    }

    public void setFlashLimit(Integer flashLimit) {
        this.flashLimit = flashLimit;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productRelationId=").append(productRelationId);
        sb.append(", productId=").append(productId);
        sb.append(", skuId=").append(skuId);
        sb.append(", skuCode=").append(skuCode);
        sb.append(", flashPrice=").append(flashPrice);
        sb.append(", flashStock=").append(flashStock);
        sb.append(", flashLimit=").append(flashLimit);
        sb.append(", originalPrice=").append(originalPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}