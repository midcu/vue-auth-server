package com.midcu.authsystem.jwt;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.midcu.authsystem.dto.PermissionDto;
import com.midcu.authsystem.dto.UserDetailsImpl;
import com.midcu.authsystem.dto.UserDto;
import com.midcu.authsystem.service.PermissionService;
import com.midcu.authsystem.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired
    private UsersService usersServiceImpl;

    @Autowired
    private PermissionService permissionServiceImpl;

    @Value("${midcu.authsystem.security.expirationTime:86400000L}") public Long expirationTime;

    @Value("${midcu.authsystem.security.jwtSecret:null}") public String jwtSecret;

    @Value("${midcu.authsystem.security.tokenHeader:Authorization}") public String tokenHeader;

    public String generateJwtToken(UserDto userDto) {

        String token = Jwts.builder().signWith(getKeySecret())
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(userDto.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();

        return token;
    }

    public UserDetailsImpl getAuthenticationToken(HttpServletRequest request) {

        String authToken = getJwtToken(request);

        if (authToken == null) {
            return null;
        }
        try {

            Jws<Claims> parsedToken = Jwts.parserBuilder().setSigningKey(getKeySecret()).build().parseClaimsJws(authToken);

            String username = parsedToken.getBody().getSubject();

            UserDto userDto = usersServiceImpl.findUserByUsername(username, UserDto.class);

            if (userDto.getState() != 1) {
                return null;
            }

            UserDetailsImpl userDetails = new UserDetailsImpl();

            userDetails.setId(userDto.getId());
            userDetails.setUsername(userDto.getUsername());
            userDetails.setStatus(userDto.getState());

            userDetails.setPermissions(permissionServiceImpl.findPermissionByUserId(userDetails.getId(), PermissionDto.class));

            return userDetails;

            // 快过期时刷新token

        } catch (JwtException  e) {
            log.error("JwtTokenProvider", "token 错误或者过期！");

            return null;
        }
    }

    public void releaseJwtToken() {
        // 释放token 当存在状态保存如：redis时，对token进行释放操作。
    }

    public Key getKeySecret() {
        if (SecurityConstants.JWT_SECRET == null) {
            if (jwtSecret.equals("null")) {
                SecurityConstants.SET_JWT_SECRET(SecurityConstants.JWT_SECRET1);
            } else {
                SecurityConstants.SET_JWT_SECRET(Keys.hmacShaKeyFor(jwtSecret.getBytes()));
            }
        }

        return SecurityConstants.JWT_SECRET;
    }

    public String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(tokenHeader);

        if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
        }
 
        return null;
    }
}