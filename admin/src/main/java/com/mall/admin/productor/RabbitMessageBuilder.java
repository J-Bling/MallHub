package com.mall.admin.productor;

public class RabbitMessageBuilder<T> {
    private T payload;
    private String queue;
    private String method;

    public RabbitMessageBuilder<T> addPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public RabbitMessageBuilder<T> addQueue(String queue) {
        this.queue = queue;
        return this;
    }

    public RabbitMessageBuilder<T> addMethod(String method) {
        this.method = method;
        return this;
    }

    public RabbitMessage<T> build() {
        return new RabbitMessage<>(payload, queue, method);
    }
}