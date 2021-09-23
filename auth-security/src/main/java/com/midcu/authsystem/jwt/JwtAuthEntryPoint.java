package com.midcu.authsystem.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)  throws IOException, ServletException {

        log.info("JwtAuthEntryPoint:" + e.getMessage());

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "禁止访问！");
    }
}
