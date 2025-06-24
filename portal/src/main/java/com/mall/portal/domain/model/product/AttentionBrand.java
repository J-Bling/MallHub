package com.mall.portal.domain.model.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document
public class AttentionBrand implements Serializable {
    @Id
    private String id;
    @Indexed
    private Long memberId;
    private String memberNickname;
    private String memberIcon;
    @Indexed
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String brandStory;
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

    public String getBrandStory() {
        return brandStory;
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

    public void setBrandStory(String brandStory) {
        this.brandStory = brandStory;
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
