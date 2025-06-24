package com.mall.portal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializationConfig {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
