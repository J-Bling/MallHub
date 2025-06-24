package com.mall.admin.productor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.constant.enums.queues.QueueEnum;
import com.mall.common.domain.RabbitRpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.util.function.Consumer;
import java.util.Date;

public interface RabbitManage {
    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(RabbitManage.class);

    RabbitTemplate getRabbitTemplate();

    default String serialization(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    default CorrelationData setCallBack(String method) {
        CorrelationData correlationData = new CorrelationData(method);
        correlationData.getFuture().addCallback(
                c -> {
                    if (c != null && c.isAck()) {
                        logger.info("调用{}成功 时间{}", method, new Date());
                    } else {
                        logger.info("调用{}失败 时间{}", method, new Date());
                    }
                },
                e -> logger.error("调用{}发生异常:{}", method, e.getMessage())
        );
        return correlationData;
    }

    default <T extends RabbitRpcMessage> void sendMessage(
            QueueEnum queue,
            String method,
            Consumer<T> builderConsumer,
            T messageInstance) throws JsonProcessingException {

        builderConsumer.accept(messageInstance);
        messageInstance.addQueue(queue.getQueueName()).addMethod(method);

        getRabbitTemplate().convertAndSend(
                queue.getExchange(),
                queue.getRouteKey(),
                serialization(messageInstance),
                setCallBack(method)
        );
    }
}
