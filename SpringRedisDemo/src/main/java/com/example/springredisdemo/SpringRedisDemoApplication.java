package com.example.springredisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class SpringRedisDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisDemoApplication.class, args);
    }

}
