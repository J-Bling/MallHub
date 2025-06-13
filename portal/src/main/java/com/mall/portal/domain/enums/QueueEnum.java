package com.mall.portal.domain.enums;

/**
 * 消息队列
 */
public enum QueueEnum {
    /**
     * 消息通知队列
     */
    QUEUE_ORDER_CANCEL("mall.order.direct", "mall.order.cancel", "mall.order.cancel"),
    /**
     * 消息通知ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("mall.order.direct.ttl", "mall.order.cancel.ttl", "mall.order.cancel.ttl");


    private String exchange;
    private String queueName;
    private String routeKey;

    QueueEnum(String exchange,String queueName,String routeKey){
        this.exchange= exchange;
        this.queueName=queueName;
        this.routeKey=routeKey;
    }

    public String getExchange() {
        return exchange;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getRouteKey() {
        return routeKey;
    }
}
