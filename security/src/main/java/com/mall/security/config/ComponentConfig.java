package com.mall.security.config;

import com.mall.security.component.JwtAuthenticationTokenFilter;
import com.mall.security.component.RestAuthenticationEntryPoint;
import com.mall.security.component.RestfulAccessDeniedHandler;
import com.mall.security.component.dynamic.DynamicAccessDecisionManager;
import com.mall.security.component.dynamic.DynamicSecurityFilter;
import com.mall.security.component.dynamic.DynamicSecurityMetadataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ComponentConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WhitelistUrls whitelistUrls(){
        return new WhitelistUrls();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler(){
        return new RestfulAccessDeniedHandler();
    }

    @ConditionalOnBean(name = "DynamicSecurityService")
    @Bean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager(){
        return new DynamicAccessDecisionManager();
    }

    @ConditionalOnBean(name = "DynamicSecurityService")
    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource(){
        return new DynamicSecurityMetadataSource();
    }

    @ConditionalOnBean(name = "DynamicSecurityService")
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter(){
        return new DynamicSecurityFilter();
    }

}
