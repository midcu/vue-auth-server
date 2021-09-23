package com.midcu.authsystem;

import com.midcu.authsystem.jwt.JwtAccessDeniedHandler;
import com.midcu.authsystem.jwt.JwtAuthEntryPoint;
import com.midcu.authsystem.jwt.JwtAuthorizationFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public JwtAuthorizationFilter authorizationJwtTokenFilter() {
		return new JwtAuthorizationFilter();
	}

	@Bean
	public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
		return new JwtAccessDeniedHandler();
	}

	@Bean
	public JwtAuthEntryPoint jwtAuthEntryPoint() {
		return new JwtAuthEntryPoint();
	}

	@Value("${midcu.authsystem.security.permitUrl:Null}") private String permitUrls;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.formLogin().disable().httpBasic().disable().csrf().disable();
		
		if (!permitUrls.equals("Null")) {
			log.info("无需验证的路径：" + permitUrls);
			http.authorizeRequests().antMatchers(permitUrls.split(",")).permitAll();
		}


		http.antMatcher("/**").addFilterBefore(authorizationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests().anyRequest().authenticated()
			.and().exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler())
			.authenticationEntryPoint(jwtAuthEntryPoint());
		
	}

	@Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> registration(JwtAuthorizationFilter filter) {
        FilterRegistrationBean<JwtAuthorizationFilter> registration = new FilterRegistrationBean<JwtAuthorizationFilter>(filter);
        registration.setEnabled(false);
        return registration;
    }

}
