package com.mall.portal.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"com.mall.mbg.mapper", "com.mall.portal.dao"})
public class MybatisConfig {

}
