package com.mall.admin.config;

import com.mall.common.constant.enums.queues.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    DirectExchange productDirect(){
        return new DirectExchange(QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),true,false);
    }

    @Bean
    DirectExchange promotionDirect(){
        return new DirectExchange(QueueEnum.QUEUE_PROMOTION_HANDLE.getExchange(),true,false);
    }

    /**
     *商品队列
     */
    @Bean
    public Queue productQueue(){
        return new Queue(QueueEnum.QUEUE_PRODUCT_HANDLE.getQueueName(),true,false,false);
    }

    /**
     * 活动队列
     */
    @Bean
    public Queue promotionQueue(){
        return new Queue(QueueEnum.QUEUE_PROMOTION_HANDLE.getQueueName(),true,false,false);
    }

    @Bean
    Binding productBinding(DirectExchange productDirect, Queue productQueue){
        return BindingBuilder
                .bind(productQueue)
                .to(productDirect)
                .with(QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey());
    }

    @Bean
    Binding promotionBinding(DirectExchange promotionDirect,Queue promotionQueue){
        return BindingBuilder
                .bind(promotionQueue)
                .to(promotionDirect)
                .with(QueueEnum.QUEUE_PROMOTION_HANDLE.getRouteKey());
    }
}
