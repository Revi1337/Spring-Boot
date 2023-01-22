package com.example.springsecurityauthenticationmanagerandauthenticationprovider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .formLogin()
                .and()
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user")
                        .password("{noop}1111")
                        .roles("USER")
                        .build(),
                User.withUsername("sys")
                        .password("{noop}1111")
                        .roles("USER", "SYS")
                        .build(),
                User.withUsername("admin")
                        .password("{noop}1111")
                        .roles("USER", "ADMIN")
                        .build()
        );
    }
}
