package com.example.sc.schedule.config;

import com.example.sc.schedule.SshKeepAliveJobBean;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class SchedulerConfig {
    private final Scheduler scheduler;
    private final CustomScheduleProperties customScheduleProperties;

    @SneakyThrows
    @PostConstruct
    public void init() {
        JobDetail sshJobBean = quartzJobDetail(SshKeepAliveJobBean.class, new HashMap());
        if (customScheduleProperties.getSshKeepAliveRun())
            scheduler.scheduleJob(sshJobBean, jobTrigger(customScheduleProperties.getSshKeepAlive()));
    }


    private JobDetail quartzJobDetail(Class c, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);
        return JobBuilder.newJob(c)
                .usingJobData(jobDataMap)
                .build();
    }

    private Trigger jobTrigger(String scheduleCron) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleCron))
                .build();
    }

    private Trigger simpleTrigger(int hours) {
        return TriggerBuilder.newTrigger()
                .startAt(DateBuilder.evenHourDateAfterNow())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(hours)
                        .repeatForever())
                .build();
    }
}
