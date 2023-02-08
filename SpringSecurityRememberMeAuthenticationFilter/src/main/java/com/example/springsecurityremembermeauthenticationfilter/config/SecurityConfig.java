package com.example.springsecurityremembermeauthenticationfilter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("asdf")
                        .password("{noop}asdf")
                        .roles("USER")
                        .build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember")                        // 기본 파라미터명은  remember-me
                        .tokenValiditySeconds(3600)                             // Default 는 14일
                        .alwaysRemember(false)                                  // 리멤버 미 기능이 체크되어 있지 않아도 항상 실행 (디폴트는 false 임 -> 당연히 안한느게 낫겠지?)
                        .userDetailsService(userDetailsService()))              // 리멤버 미 인증을할 때 꼭 명시해주어야함. (무조건 써야함)
                .build();
    }

}
