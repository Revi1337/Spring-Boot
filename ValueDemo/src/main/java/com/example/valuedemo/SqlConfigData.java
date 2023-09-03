package com.example.valuedemo;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor @Builder @Getter @Slf4j
public class SqlConfigData {

    private String username;
    private String password;
    private int port;
    private List<String> options;
    private Duration timeout;

    @PostConstruct
    public void init() {
        log.info("======= SqlConfigData =======");
        log.info("username = {}", username);
        log.info("password = {}", password);
        log.info("port = {}", port);
        log.info("options = {}", options);
        log.info("timeout = {}", timeout);
    }
}
