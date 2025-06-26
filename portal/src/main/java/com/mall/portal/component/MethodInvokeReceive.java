package com.mall.portal.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.domain.MethodInvokeMessage;
import com.mall.common.domain.ParamItem;
import com.mall.security.util.SpringBeanUtil;
import com.rabbitmq.client.Channel;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@RabbitListener(queues = "mall.remote.queue")
public class MethodInvokeReceive {

    @Autowired private SpringBeanUtil springBeanUtil;
    @Autowired private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(MethodInvokeReceive.class);


    @RabbitHandler
    public void handle(Message message , Channel channel){
        String body = new String(message.getBody());
        try {
            MethodInvokeMessage invokeMessage = objectMapper.readValue(body, objectMapper.constructType(MethodInvokeMessage.class));
            Object bean = springBeanUtil.getBean(invokeMessage.getBeanName());
            Method method = bean.getClass().getMethod(invokeMessage.getMethodName());
            Parameter[] parameters = method.getParameters();
            if ((parameters.length >0 && invokeMessage.getParamItemList()==null)  || parameters.length != invokeMessage.getParamItemList().size()){
                logger.error("调用方填入参数无效:{}",body);
            }
            Object[] params = new Object[parameters.length];
            if (!Collections.isEmpty(invokeMessage.getParamItemList())){
                for (int i=0;i<invokeMessage.getParamItemList().size();i++){
                    ParamItem item = invokeMessage.getParamItemList().get(i);
                    if (item==null){
                        params[i] = null;
                    }else {
                        params[i] =objectMapper.readValue(item.getParamValue().toString(),objectMapper.constructType(Class.forName(item.getParamType())));
                    }
                }
            }
            method.invoke(bean,params);

        }catch (Exception e){
            logger.error("调用失败:{}",e.getMessage());
        }
    }
}