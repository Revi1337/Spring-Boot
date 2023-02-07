package com.example.springsecuritylogoutfilter.config.logoutsuccesshandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("First CustomLogoutSuccessHandler");
        log.info("The User {} logout successful", authentication.getName());
        super.onLogoutSuccess(request, response, authentication);
    }
}
