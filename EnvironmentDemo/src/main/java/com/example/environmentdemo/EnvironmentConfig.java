package com.example.environmentdemo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.util.List;

@Slf4j @RequiredArgsConstructor
@Configuration
public class EnvironmentConfig {

    private final Environment environment;

    @Bean
    public SqlConfigData environmentValues() {
        String username = environment.getProperty("env.values.environment.username");
        String password = environment.getProperty("env.values.environment.password");
        int port = environment.getProperty("env.values.environment.port", Integer.class);
        List<String> options = environment.getProperty("env.values.environment.options", List.class);
        Duration timeout = environment.getProperty("env.values.environment.timeout", Duration.class);

        return SqlConfigData.builder()
                .username(username)
                .password(password)
                .port(port)
                .options(options)
                .timeout(timeout)
                .build();
    }

}
