package com.example.valuedemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class SqlConfiguration {

    @Value("${env.values.username:DUMMYUSER}") private String username;
    @Value("${env.values.password}") private String password;
    @Value("${env.values.port}") private int port;
    @Value("${env.values.options}") private List<String> options;
    @Value("${env.values.timeout}") private Duration timeout;

    @Bean
    public SqlConfigData sqlConfigData() {
        return SqlConfigData.builder()
                .username(username)
                .password(password)
                .port(port)
                .options(options)
                .timeout(timeout)
                .build();
    }
}
