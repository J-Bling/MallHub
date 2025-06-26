package com.mall.admin.config;

import com.mall.common.constant.enums.queues.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    /**
     * 远程调用交换机
     */
    public DirectExchange remoteExchange(){
        return new DirectExchange(QueueEnum.REMOTE_INVOKE_HANDLE.getExchange());
    }

    /**
     * 远程调用队列
     */
    public Queue remoteQueue(){
        return new Queue(QueueEnum.REMOTE_INVOKE_HANDLE.getQueueName());
    }


    public Binding remoteBinding(DirectExchange remoteExchange,Queue remoteQueue){
        return BindingBuilder
                .bind(remoteQueue)
                .to(remoteExchange)
                .with(QueueEnum.REMOTE_INVOKE_HANDLE.getRouteKey());
    }
}
