package com.example.springemailsender.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

// 이메일이 유효한지 검증
@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        // TODO: Regex to Validate Email
        return true;
    }

}
