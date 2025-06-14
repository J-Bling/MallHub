package com.mall.portal.domain.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class AttentionProduct {
    @Id
    private String id;
    @Indexed
    private Long memberId;
    private String memberNickname;
    private String memberIcon;
    @Indexed
    private Long productId;
    private String productName;
    private String productPic;
    private String productSubTitle;
    private String productPrice;
    private Date createTime;

    public String getMemberIcon() {
        return memberIcon;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPic() {
        return productPic;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductSubTitle() {
        return productSubTitle;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductSubTitle(String productSubTitle) {
        this.productSubTitle = productSubTitle;
    }
}
