package com.mall.portal.config;

import com.mall.common.constant.enums.queues.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    DirectExchange orderDirect(){
        return new DirectExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange(),true,false);
    }

    @Bean
    DirectExchange orderTtlDirect(){
        return new DirectExchange(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),true,false);
    }




    /**
     *订单消费队列
     */
    @Bean
    public Queue orderQueue(){
        return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getQueueName(),true,false,false);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue orderTtlQueue(){
        return QueueBuilder
                .durable(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getQueueName())
                .withArgument("x-dead-letter-exchange",QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .withArgument("x-dead-letter-routing-key",QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey())
                .build();
    }




    @Bean
    Binding orderBinding(DirectExchange orderDirect , Queue orderQueue){
        return BindingBuilder
                .bind(orderQueue)
                .to(orderDirect)
                .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    }

    @Bean
    Binding orderTtlBinding(DirectExchange orderTtlDirect,Queue orderTtlQueue){
        return BindingBuilder
                .bind(orderTtlQueue)
                .to(orderTtlDirect)
                .with(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey());
    }

}
