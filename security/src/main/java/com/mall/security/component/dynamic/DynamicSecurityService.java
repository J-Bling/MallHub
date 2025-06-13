package com.mall.security.component.dynamic;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * 动态权限相关接口
 */
public interface DynamicSecurityService {
    Map<String, ConfigAttribute> loadDataSource();
}