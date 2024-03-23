package com.j4.krevetka.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
public class TaskSchedulerConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar){
        taskRegistrar.setScheduler(taskExecutor());
    }

    public ScheduledExecutorService taskExecutor(){
        return Executors.newScheduledThreadPool(1);
    }
}
