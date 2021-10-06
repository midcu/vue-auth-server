package com.midcu.authsystem.service;

import com.midcu.authsystem.dto.PermissionDto;
import com.midcu.authsystem.dto.UserDetailsImpl;
import com.midcu.authsystem.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UsersService usersServiceImpl;

    @Autowired
    private PermissionService permissionServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        UserDto userDto = usersServiceImpl.findUserByUsername(username, UserDto.class);

        UserDetailsImpl userDetails = new UserDetailsImpl();

        if (userDto == null) {
            throw new UsernameNotFoundException("用户名或密码错误！");
        }

        userDetails.setId(userDto.getId());
        userDetails.setUsername(userDto.getUsername());
        userDetails.setStatus(userDto.getState());
        userDetails.setPassword(userDto.getPassword());

        userDetails.setPermissions(permissionServiceImpl.findPermissionByUserId(userDetails.getId(), PermissionDto.class));

        return userDetails;
    }
    
}
