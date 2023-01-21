package com.example.springsecuritysecuritycontextmechanism.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String index(HttpSession httpSession) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);// SPRING_SECURITY_CONTEXT_KEY 로 세션에 SecurityContext 를 저장했기 때문에 그대로 갖고올 수 있음.
        Authentication authentication1 = securityContext.getAuthentication();

        if (authentication.equals(authentication1))
            System.out.println("true");

        return "home";
    }

    @GetMapping("/thread")
    public String thread() {
        new Thread(() -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        });

        return "thread";
    }



}