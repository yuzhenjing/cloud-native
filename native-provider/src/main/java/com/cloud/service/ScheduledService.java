package com.cloud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;

@Slf4j
//@Configuration
@EnableScheduling
public class ScheduledService implements SchedulingConfigurer {

    @Autowired
    private Environment environment;

    public void test() {
        try {
            Thread.sleep(2000L);
            log.info(LocalDateTime.now().toString() + "执行定时任务......");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this::test, triggerContext -> {
            String cron = environment.getProperty("schedule.cron");
            CronTrigger cronTrigger = new CronTrigger("*/" + cron);
            return cronTrigger.nextExecutionTime(triggerContext);
        });
    }

}
