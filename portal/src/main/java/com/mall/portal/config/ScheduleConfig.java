package com.mall.portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    private final Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setThreadNamePrefix("scheduled-task-");
        taskScheduler.setErrorHandler(e->{
            logger.error("定时任务执行发送异常:{}",e.getMessage());
        });
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }
}
