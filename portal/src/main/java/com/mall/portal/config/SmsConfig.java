package com.mall.portal.config;

import com.mall.common.util.SmsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Bean
    public SmsUtil smsUtil(){
        return new SmsUtil() {
            @Override
            public String generateCode() {
                return "";
            }

            @Override
            public void send(String phone, String code) {

            }
        };
    }
}
