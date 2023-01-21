package com.example.springsecurityauthorizeexpression.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializeData1 {

    private static final Logger log = LoggerFactory.getLogger(InitializeData1.class);

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            log.info("4");
            log.info("5");
            log.info("6");
        };
    }

}
