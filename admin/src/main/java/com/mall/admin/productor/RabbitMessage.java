package com.mall.admin.productor;

import lombok.Data;

@Data
public class RabbitMessage<T> {
    private final T data;
    private final String queue;
    private final String method;

    public RabbitMessage(T data, String queue, String method) {
        this.data = data;
        this.queue = queue;
        this.method = method;
    }

    public T getData() { return data; }
    public String getQueue() { return queue; }
    public String getMethod() { return method; }
}
