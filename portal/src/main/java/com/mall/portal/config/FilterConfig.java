package com.mall.portal.config;
import com.mall.portal.filter.KryoRemoveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
    @Autowired private KryoRemoveFilter kryoRemoveFilter;

    @Bean
    public FilterRegistrationBean<KryoRemoveFilter> myCustomFilterRegistration() {
        FilterRegistrationBean<KryoRemoveFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(kryoRemoveFilter);
        registration.addUrlPatterns("/*"); // 过滤所有URL
        registration.setName(KryoRemoveFilter.class.getName());
        registration.setOrder(Ordered.LOWEST_PRECEDENCE); // 设置执行顺序
        return registration;
    }
}
