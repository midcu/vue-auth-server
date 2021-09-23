package com.midcu.authsystem.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.midcu.authsystem.dto.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, AccessDeniedException {
                
        UsernamePasswordAuthenticationToken authenticationToken = null;

        UserDetailsImpl userDetails = jwtTokenProvider.getAuthenticationToken(getJwtToken(request));

        if (userDetails != null) {
            authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        response.addHeader("Authorization", "arg1");

        filterChain.doFilter(request, response);
    }
 
    private String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(SecurityConstants.TOKEN_HEADER);

        if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
        }
 
        return null;
    }
}
