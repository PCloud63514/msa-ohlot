package com.example.client.member.config;

import com.example.client.member.MemberClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Configuration
public class ClientConfig {

    @Bean
    public MemberClient memberClient(
            @Autowired ObjectMapper objectMapper,
            @Value("${client.member.url}") String url,
            @Autowired FormattingConversionService feignConversionService) {

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new SpringFormEncoder(new JacksonEncoder(objectMapper)))
                .decoder(new JacksonDecoder(objectMapper))
                .contract(new SpringMvcContract(new ArrayList<>(), feignConversionService))
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .target(MemberClient.class, url);
    }

    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegister() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            registrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
//            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }

    @Bean
    public FormattingConversionService feignConversionService() {
        FormattingConversionService service = new DefaultFormattingConversionService();
        localDateFeignFormatterRegister().registerFormatters(service);

        return service;
    }
}
