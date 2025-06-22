package com.mall.common.domain;

public class RabbitRpcMessage {
    private String queue;
    private String method;

    public static RabbitRpcMessage Builder(){
        return new RabbitRpcPromotionMessage();
    }

    public RabbitRpcMessage addQueue(String queue){
        this.queue=queue;
        return this;
    }

    public RabbitRpcMessage addMethod(String method){
        this.method=method;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public String getQueue() {
        return queue;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
