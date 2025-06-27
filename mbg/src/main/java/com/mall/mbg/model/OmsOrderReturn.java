package com.mall.mbg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OmsOrderReturn implements Serializable {
    private Long id;

    private Long orderId;

    private Long orderItemId;

    private String returnNo;

    private Date applyTime;

    private Long returnReasonId;

    private String description;

    private String returnLogistics;

    private String returnLogisticsNo;

    private String returnProof;

    private Byte status;

    private String reviewNotes;

    private String reviewer;

    private Date reviewTime;

    private BigDecimal returnAmount;

    private BigDecimal deductionAmount;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Long getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(Long returnReasonId) {
        this.returnReasonId = returnReasonId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReturnLogistics() {
        return returnLogistics;
    }

    public void setReturnLogistics(String returnLogistics) {
        this.returnLogistics = returnLogistics;
    }

    public String getReturnLogisticsNo() {
        return returnLogisticsNo;
    }

    public void setReturnLogisticsNo(String returnLogisticsNo) {
        this.returnLogisticsNo = returnLogisticsNo;
    }

    public String getReturnProof() {
        return returnProof;
    }

    public void setReturnProof(String returnProof) {
        this.returnProof = returnProof;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", orderItemId=").append(orderItemId);
        sb.append(", returnNo=").append(returnNo);
        sb.append(", applyTime=").append(applyTime);
        sb.append(", returnReasonId=").append(returnReasonId);
        sb.append(", description=").append(description);
        sb.append(", returnLogistics=").append(returnLogistics);
        sb.append(", returnLogisticsNo=").append(returnLogisticsNo);
        sb.append(", returnProof=").append(returnProof);
        sb.append(", status=").append(status);
        sb.append(", reviewNotes=").append(reviewNotes);
        sb.append(", reviewer=").append(reviewer);
        sb.append(", reviewTime=").append(reviewTime);
        sb.append(", returnAmount=").append(returnAmount);
        sb.append(", deductionAmount=").append(deductionAmount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}