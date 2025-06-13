package com.mall.portal.component;

import com.mall.security.component.dynamic.DynamicSecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicSecurity implements DynamicSecurityService {
    private final Map<String,ConfigAttribute> configAttributeMap = new HashMap<>();

    @Value("filterUrls.allPortal")
    private String allEntry;

    @Value("userRole.consumer")
    private String consumer;

    @Override
    public Map<String, ConfigAttribute> loadDataSource() {
        configAttributeMap.put(allEntry,new SecurityConfig(consumer));
        return configAttributeMap;
    }
}
