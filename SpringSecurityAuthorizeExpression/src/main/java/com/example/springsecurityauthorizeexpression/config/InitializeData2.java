package com.example.springsecurityauthorizeexpression.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializeData2 implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InitializeData2.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("CommandLineRunner Running... :)");
    }
}
