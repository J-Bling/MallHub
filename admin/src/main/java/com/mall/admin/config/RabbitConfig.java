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
    public DirectExchange productDirect(){
        return new DirectExchange(QueueEnum.QUEUE_PRODUCT_HANDLE.getExchange(),true,false);
    }

    @Bean
    public DirectExchange promotionDirect(){
        return new DirectExchange(QueueEnum.QUEUE_PROMOTION_HANDLE.getExchange(),true,false);
    }

    @Bean
    public DirectExchange productAttributeDirect(){
        return new DirectExchange(QueueEnum.PRODUCT_ATTRIBUTE_HANDLE.getExchange(),true,false);
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

    /**
     * 商品属性队列
     */
    @Bean
    public Queue productAttributeQueue(){
        return new Queue(QueueEnum.PRODUCT_ATTRIBUTE_HANDLE.getQueueName(),true,false,false);
    }




    @Bean
    public Binding productBinding(DirectExchange productDirect, Queue productQueue){
        return BindingBuilder
                .bind(productQueue)
                .to(productDirect)
                .with(QueueEnum.QUEUE_PRODUCT_HANDLE.getRouteKey());
    }

    @Bean
    public Binding promotionBinding(DirectExchange promotionDirect,Queue promotionQueue){
        return BindingBuilder
                .bind(promotionQueue)
                .to(promotionDirect)
                .with(QueueEnum.QUEUE_PROMOTION_HANDLE.getRouteKey());
    }


    @Bean
    public Binding productAttributeBinding(DirectExchange productAttributeDirect,Queue productAttributeQueue){
        return BindingBuilder
                .bind(productAttributeQueue)
                .to(productAttributeDirect)
                .with(QueueEnum.PRODUCT_ATTRIBUTE_HANDLE.getRouteKey());
    }
}
