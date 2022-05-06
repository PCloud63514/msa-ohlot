package com.example.sc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("ssh-connection")
public class SshSessionProperties {
    private List<SshSessionProperty> list;
}
