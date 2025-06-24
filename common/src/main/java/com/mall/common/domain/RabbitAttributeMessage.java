package com.mall.common.domain;


public class RabbitAttributeMessage extends RabbitRpcMessage{
    private Long attributeId;
    private Long categoryId;

    public RabbitAttributeMessage(){}
    public RabbitAttributeMessage(String queue,String method,Long attributeId,Long categoryId){
        super();
        this.attributeId=attributeId;
        this.categoryId=categoryId;
        this.setQueue(queue);
        this.setMethod(method);
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
