package com.example.springsecuritylogoutfilter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
             .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .formLogin()
                .and()
                .logout(logout -> logout
                        .logoutUrl("/logout")                                       // 로그아웃 페이지 설정
                        .logoutSuccessUrl("/login")                                 // 로그아웃 성공시 이동경로. (logoutSuccessHandler 랑 같이있으면 무시됨. --> 우선순위가 밀림.)
                        .addLogoutHandler((req, res, auth) -> {                     // 로그아웃시 실행할 Handler 명시
                            HttpSession httpSession = req.getSession();
                            httpSession.invalidate();
                        })
                        .logoutSuccessHandler((req, res, auth) -> {                 // 로그아웃 성고시 수행할 Handler 명시
                            res.sendRedirect("/1");
                        })
                        .deleteCookies("remember-me", "JSESSIONID")) // 쿠키 삭제
                .build();
    }

}
