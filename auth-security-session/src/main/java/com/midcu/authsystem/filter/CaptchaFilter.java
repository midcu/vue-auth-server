package com.midcu.authsystem.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.midcu.authsystem.web.vo.ResponseVo;
import com.wf.captcha.utils.CaptchaUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

public class CaptchaFilter extends OncePerRequestFilter{

    @Value("${midcu.authsystem.security.captchaCodeName:captchaCode}") private String captchaCodeName;

    @Value("${midcu.authsystem.security.loginUrl:/login}") private String loginUrl;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, AccessDeniedException {
        
        if (!StringUtils.equalsIgnoreCase(loginUrl, request.getRequestURI()) || !StringUtils.equalsIgnoreCase("post", request.getMethod())) {
            // 非Login POST方法
            filterChain.doFilter(request, response);
        } else {
            String codeName = request.getParameter(captchaCodeName);
    
             if (StringUtils.isBlank(codeName)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
    
                response.getWriter().print(new ResponseVo("未填写验证码！").toString());
                
            } else if (!CaptchaUtil.ver(codeName, request)) {
                CaptchaUtil.clear(request);
    
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
    
                response.getWriter().print(new ResponseVo("验证码错误！").toString());
            } else {
                CaptchaUtil.clear(request);
                filterChain.doFilter(request, response);
            }
        }


    }
 
}
