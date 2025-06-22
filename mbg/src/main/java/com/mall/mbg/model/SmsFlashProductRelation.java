package com.mall.mbg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SmsFlashProductRelation implements Serializable {
    private Long id;

    private Long promotionId;

    private Long sessionId;

    private Long productId;

    private Boolean isSku;

    private BigDecimal flashPrice;

    private Integer flashStock;

    private Integer flashLimit;

    private BigDecimal originalPrice;

    private Integer sort;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getIsSku() {
        return isSku;
    }

    public void setIsSku(Boolean isSku) {
        this.isSku = isSku;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
        sb.append(", promotionId=").append(promotionId);
        sb.append(", sessionId=").append(sessionId);
        sb.append(", productId=").append(productId);
        sb.append(", isSku=").append(isSku);
        sb.append(", flashPrice=").append(flashPrice);
        sb.append(", flashStock=").append(flashStock);
        sb.append(", flashLimit=").append(flashLimit);
        sb.append(", originalPrice=").append(originalPrice);
        sb.append(", sort=").append(sort);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}