package com.midcu.authsystem;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.midcu.authsystem.filter.CaptchaFilter;
import com.midcu.authsystem.web.rp.JsonResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${midcu.authsystem.security.permitUrl:Null}") private String permitUrls;

	@Value("${midcu.authsystem.security.captcha:true}") private Boolean captcha;

	@Bean
	public CaptchaFilter captchaFilter() {
		return new CaptchaFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().disable().csrf().disable();

		if (captcha) {
			// 是否开启验证码拦截
			http.addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class);
		}

		http.formLogin().successHandler(new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				JsonResponse.setJsonResult(response, "登录成功！", HttpServletResponse.SC_OK);
			}
			
		}).failureHandler(new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				JsonResponse.setJsonResult(response, exception.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
			}
			
		}).permitAll();

		http.logout().logoutSuccessHandler(new LogoutSuccessHandler() {

			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				JsonResponse.setJsonResult(response, "退出成功！", HttpServletResponse.SC_OK);
			}
			
		}).permitAll();


		if (!permitUrls.equals("Null")) {
			log.info("无需验证的路径：" + permitUrls);
			http.authorizeRequests().antMatchers(permitUrls.split(",")).permitAll();
		}

		http.authorizeRequests().anyRequest().authenticated();

		http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				// 访问未授权资源时，返回401
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "没有操作权限！");
			}
			
		})
		.authenticationEntryPoint(new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				// 未登录或者登录超时，返回403
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "禁止访问！");
			}
			
		});
		
	}

	@Bean
    public FilterRegistrationBean<CaptchaFilter> registration(CaptchaFilter filter) {
        FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<CaptchaFilter>(filter);
        registration.setEnabled(false);
        return registration;
    }

}
