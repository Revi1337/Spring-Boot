package com.example.springsecurityauthenticationflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class SpringSecurityAuthenticationFlowApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringSecurityAuthenticationFlowApplication.class, args);
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
    }

}
