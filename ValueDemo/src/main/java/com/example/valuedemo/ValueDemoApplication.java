package com.example.valuedemo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication @RequiredArgsConstructor @Slf4j
public class ValueDemoApplication {

    private final SqlConfigData sqlConfigData;

    @PostConstruct
    public void init() {
        log.info("Injected USERNAME = {}", sqlConfigData.getUsername());
    }

    public static void main(String[] args) {
        SpringApplication.run(ValueDemoApplication.class, args);
    }

}
