package com.example.springsecurityauthenticationmechanism.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component @Slf4j
public class BeanUtils1 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("CommandLineRunner Running.. :)");
    }
}
