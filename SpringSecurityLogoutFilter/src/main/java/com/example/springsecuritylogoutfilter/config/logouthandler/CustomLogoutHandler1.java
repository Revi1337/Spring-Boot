package com.example.springsecuritylogoutfilter.config.logouthandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class CustomLogoutHandler1 implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("First CustomLogoutHandler");
        request.getSession().invalidate();
    }
}
