package com.mall.portal.domain.model;

import java.util.Date;

public class AttentionBrand {
    private String id;
    private Long memberId;
    private String memberNickname;
    private String memberIcon;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String brandCity;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getBrandCity() {
        return brandCity;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getId() {
        return id;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBrandCity(String brandCity) {
        this.brandCity = brandCity;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }
}
