package com.mall.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class SmsFlashSession implements Serializable {
    private Long id;

    private Long promotionId;

    private String name;

    private String coverUrl;

    private Date startTime;

    private Date endTime;

    private Boolean repeatType;

    private Boolean status;

    private Date createTime;

    private String styleConfig;

    private String rulesConfig;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(Boolean repeatType) {
        this.repeatType = repeatType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStyleConfig() {
        return styleConfig;
    }

    public void setStyleConfig(String styleConfig) {
        this.styleConfig = styleConfig;
    }

    public String getRulesConfig() {
        return rulesConfig;
    }

    public void setRulesConfig(String rulesConfig) {
        this.rulesConfig = rulesConfig;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", promotionId=").append(promotionId);
        sb.append(", name=").append(name);
        sb.append(", coverUrl=").append(coverUrl);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", repeatType=").append(repeatType);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", styleConfig=").append(styleConfig);
        sb.append(", rulesConfig=").append(rulesConfig);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}