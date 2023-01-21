package com.example.springsecurityauthorizeexpression.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity @Slf4j
public class SecurityConfig {

    /**
     * @Describe 인가정책 설정
     * <ul>
     * <li> authenticated() : 인증된 사용자의 접근을 허용 </li>
     * <li> fullyAuthenticated() : 인증된 사용자의 접근을 허용하지만, rememberMe 인증 제외 (Form 인증 방식만 허용) </li>
     * <li> permitAll() : 무조건 접근을 허용 </li>
     * <li> denyAll() : 무조건 접근을 허용하지 않음 </li>
     * <li> ananoymous() : 익명사용자의 접근을 허용 (기본적으로 Role Anonymous 임, Role User 와 다름. User 가 Anonymous 자원에 접근 불가함)</li>
     * <li> rem emberMe() : 기억하기를 통해 인증된 사용자의 접근을 허용 </li>
     * <li> access(String) : 주어진 SpEL 표현식의 평가 결과가 true 이면 접근을 허용 </li>
     * <li> hasRole(String) : 사용자가 주어진 역할이 있다면 접근을 허용 (Prefix 로  ROLE 이 붙지 않음)</li>
     * <li> hasAuthority(String) : 사용자가 주어진 권한이 있다면.. (Prefix 로  ROLE 이 붙음)</li>
     * <li> hasAnyRole(String) : 사용자가 주어진 권한이 있다면 접근을 허용 </li>
     * <li> hasAnyAuthority(String...) : 사용자가 주어진 권한  중 어떤 것이라도 있다면 접근을 허용 </li>
     * <li> hasIpAddress(String) : 주어진 IP 로부터 요청이 왔다면 접근을 허용. </li>
     * </ul>
     * @param httpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeRequests(auth -> auth
                        .antMatchers("/login").permitAll()
                        .antMatchers("/user").hasRole("USER")
                        .antMatchers("/admin/pay").hasRole("ADMIN")
                        .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler((req, res, auth) -> {
                            RequestCache requestCache = new HttpSessionRequestCache();  // TODO 인증이 성공하면 인증전에 사용자가 가고싶어헀던 정보(캐시)는 세션에 담겨있고, 이 캐시를 세션으로부터 꺼내오는 것임.
                            SavedRequest savedRequest = requestCache.getRequest(req, res);
                            res.sendRedirect(savedRequest.getRedirectUrl());
                        })
                )
                .exceptionHandling(except -> except
//                        .authenticationEntryPoint((req, res, exception) -> {
//                            res.sendRedirect("/login"); // TODO 인증실패가 나면 스프링 디폴트페이지가 아닌, 우리가 만든 /login 페이지를 보냄. --> 없으면 404
//                        })
                        .accessDeniedHandler((req, res, exception) -> {
                            res.sendRedirect("/denied");
                        })
                )
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
                        .roles("SYS", "USER")
                        .build(),
                User.withUsername("admin")
                        .password("{noop}1111")
                        .roles("ADMIN", "SYS", "USER")
                        .build()
        );
    }

}
