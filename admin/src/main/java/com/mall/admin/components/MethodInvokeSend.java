package com.mall.admin.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.constant.enums.queues.QueueEnum;
import com.mall.common.domain.MethodInvokeMessage;
import com.mall.common.domain.ParamItem;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
public class MethodInvokeSend {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RabbitTemplate rabbitTemplate;

    private final Logger logger = LoggerFactory.getLogger(MethodInvokeSend.class);

    public void send(String beanName, String methodName, List<ParamItem> invokeMessageList){
        MethodInvokeMessage invokeMessage = new MethodInvokeMessage();
        invokeMessage.setBeanName(beanName);
        invokeMessage.setMethodName(methodName);
        try {
            if (!Collections.isEmpty(invokeMessageList)) {
                for (ParamItem item : invokeMessageList) {
                    if (item.getParamValue() instanceof Serializable) {
                        item.setParamValue(objectMapper.writeValueAsString(item.getParamValue()));
                    }
                }
                invokeMessage.setParamItemList(invokeMessageList);
            }

            rabbitTemplate.convertAndSend(
                    QueueEnum.REMOTE_INVOKE_HANDLE.getExchange(),
                    QueueEnum.REMOTE_INVOKE_HANDLE.getRouteKey(),
                    objectMapper.writeValueAsString(invokeMessage),
                    setCallBack(beanName+"."+methodName));
        }catch (Exception e){
            logger.error("调用 {} 接口 {} 方法 失败:{}",beanName,methodName,e.getMessage());
        }
    }

    private CorrelationData setCallBack(String method) {
        CorrelationData correlationData = new CorrelationData(method);
        correlationData.getFuture().addCallback(
                c -> {
                    if (c != null && c.isAck()) {
                        //调试
                        logger.info("调用{}成功 时间{}", method, new Date());
                    } else {
                        logger.info("调用{}失败 时间{}", method, new Date());
                    }
                },
                e -> logger.error("调用{}发生异常:{}", method, e.getMessage())
        );
        return correlationData;
    }
}
