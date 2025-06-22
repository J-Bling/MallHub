package com.mall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mall")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class);
    }
}
