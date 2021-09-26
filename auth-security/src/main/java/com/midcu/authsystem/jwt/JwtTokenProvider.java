package com.midcu.authsystem.jwt;

import java.util.Date;

import com.midcu.authsystem.dto.PermissionDto;
import com.midcu.authsystem.dto.UserDetailsImpl;
import com.midcu.authsystem.dto.UserDto;
import com.midcu.authsystem.service.PermissionService;
import com.midcu.authsystem.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired
    private UsersService usersServiceImpl;

    @Autowired
    private PermissionService permissionServiceImpl;

    public String generateJwtToken(UserDto userDto) {

        String token = Jwts.builder().signWith(SecurityConstants.JWT_SECRET)
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(userDto.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .compact();

        return token;
    }

    public UserDetailsImpl getAuthenticationToken(String authToken) {

        UserDetailsImpl userDetails = null;

        if (authToken != null) {
            try {

                Jws<Claims> parsedToken = Jwts.parserBuilder().setSigningKey(SecurityConstants.JWT_SECRET).build().parseClaimsJws(authToken);

                String username = parsedToken.getBody().getSubject();

                UserDto userDto = usersServiceImpl.findUserByUsername(username, UserDto.class);

                if (userDto.getState() != 1) {
                    return null;
                }

                userDetails = new UserDetailsImpl();

                userDetails.setId(userDto.getId());
                userDetails.setUsername(userDto.getUsername());
                userDetails.setStatus(userDto.getState());

                userDetails.setPermissions(permissionServiceImpl.findPermissionByUserId(userDetails.getId(), PermissionDto.class));

                // 快过期时刷新token

            } catch (JwtException  e) {
                log.error("JwtTokenProvider", "token wrong");
            }
        }

        return userDetails;
    }

    public void releaseJwtToken() {
        // 释放token 当存在状态保存如：redis时，对token进行释放操作。
    }
}