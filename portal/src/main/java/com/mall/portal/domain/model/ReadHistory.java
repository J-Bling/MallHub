package com.mall.portal.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document
public class ReadHistory implements Serializable {
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

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductPic() {
        return productPic;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSubTitle() {
        return productSubTitle;
    }

    public Long getProductId() {
        return productId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public String getId() {
        return id;
    }

    public void setProductSubTitle(String productSubTitle) {
        this.productSubTitle = productSubTitle;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public void setId(String id) {
        this.id = id;
    }
}
