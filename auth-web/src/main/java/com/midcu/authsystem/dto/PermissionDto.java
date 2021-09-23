package com.midcu.authsystem.dto;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PermissionDto implements GrantedAuthority{

    private String name;

    @Override
    public String getAuthority() {

        return this.name;
    }
    
}
