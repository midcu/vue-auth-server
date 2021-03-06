package com.midcu.authsystem.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Setter;

@Setter
public class UserDetailsImpl implements UserDetails{

    private Long id;

    private String username;
    private String password;
    private List<PermissionDto> permissions;
    private Integer status;

    @Override
    public Collection<PermissionDto> getAuthorities() {
        return this.permissions;
    }
    
    public Long getId() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1 ? true : false;
    }
    
}
