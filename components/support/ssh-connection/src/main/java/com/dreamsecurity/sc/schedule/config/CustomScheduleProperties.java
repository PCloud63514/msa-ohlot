package com.dreamsecurity.sc.schedule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ssh-connection.schedule")
public class CustomScheduleProperties {
    private String sshKeepAlive;
    private Boolean sshKeepAliveRun;
}
