package com.mall.common.constant.enums.queues;

/**
 * 消息队列
 */
public enum QueueEnum {
    /**
     * 订单通知队列
     */
    QUEUE_ORDER_CANCEL("mall.order.direct", "mall.order.cancel", "mall.order.cancel"),
    /**
     * 订单通知ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("mall.order.direct.ttl", "mall.order.cancel.ttl", "mall.order.cancel.ttl"),
    /**
     * 商品通知队列
     */
    QUEUE_PRODUCT_HANDLE("mall.product.direct","mall.product.handle","mall.product.handle"),
    /**
     * 活动通知队列
     */
    QUEUE_PROMOTION_HANDLE("mall.promotion.direct","mall.promotion.handle","mall.promotion.handle"),
    /**
     * 优惠券通知队列
     */
    COUPON_HANDLE("mall.coupon.direct","mall.coupon.queue","mall.coupon.queue");


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
