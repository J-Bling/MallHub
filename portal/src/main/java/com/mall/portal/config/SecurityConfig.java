package com.mall.portal.config;

import com.mall.portal.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig {
    @Autowired private ConsumerService consumerService;

    @Bean
    @Primary
    public UserDetailsService userDetailsService(){
        return username-> consumerService.loadUserByUsername(username);
    }
}
