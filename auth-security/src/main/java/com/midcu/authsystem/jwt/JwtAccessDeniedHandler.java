package com.midcu.authsystem.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      // 访问未授权资源时，返回403 Forbidden
      log.info("JwtAccessDeniedHandler:" + accessDeniedException.getMessage());
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "没有访问权限！");
   }
}
