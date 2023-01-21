package com.example.springsecurityauthenticationflow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmdConfig {

    private static final Logger log = LoggerFactory.getLogger(CmdConfig.class);

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            log.info("CMD RUNNER 3 Running ... :)");
        };
    }
}
