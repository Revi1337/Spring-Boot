package com.example.springsecurityauthenticationflow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CmdRunner2 implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CmdRunner2.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("CMD RUNNER 2 Running ... :)");
    }
}
