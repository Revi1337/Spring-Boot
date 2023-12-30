package com.example.springredisdemo.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;


@Component
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    void postConstruct() {
        System.out.println(redisTemplate);
    }

    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public Boolean setNx(String key, String value) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(60));
    }

    public Boolean delKey(String key) {
        return redisTemplate
                .delete(key);
    }
}
