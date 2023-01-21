package com.example.springsecurityauthenticationmechanism.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanUtils2 {

    private static final Logger log = LoggerFactory.getLogger(BeanUtils2.class);

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            log.info("Second CommandLineRunner Running... :)");
        };
    }
}
