package com.example.environmentdemo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication @RequiredArgsConstructor @Slf4j
public class EnvironmentDemoApplication {

    private final SqlConfigData sqlConfigData;

    public static void main(String[] args) {
        SpringApplication.run(EnvironmentDemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        log.info("sqlConfigData = {}", sqlConfigData);
    }

}
