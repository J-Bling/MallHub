package com.mall.portal.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.mall.portal.component.AlipayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {
    @Bean
    public AlipayClient alipayClient(AlipayProperties config){
        return new DefaultAlipayClient(config.getGatewayUrl(),config.getAppId(),
                config.getAppPrivateKey(), config.getFormat(),config.getCharset(),
                config.getAlipayPublicKey(),config.getSignType());
    }
}
