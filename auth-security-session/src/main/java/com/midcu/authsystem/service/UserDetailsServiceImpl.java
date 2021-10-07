package com.midcu.authsystem.service;

import com.midcu.authsystem.dto.PermissionDto;
import com.midcu.authsystem.dto.UserDetailsImpl;
import com.midcu.authsystem.dto.UserDto;
import com.midcu.authsystem.web.qo.PageQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    // 是否开启管理员账号
	@Value("${midcu.authsystem.security.admin:false}") private Boolean admin;

	// 管理员账号名称
	@Value("${midcu.authsystem.security.user.username:admin}") private String adminUsername;

    // 管理员账号密码
	@Value("${midcu.authsystem.security.user.password:admin}") private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (admin && username.equals(adminUsername)) {
            // 判断是否开启管理员账号功能 且是否为管理员账号
            UserDetailsImpl userDetails = new UserDetailsImpl();

            userDetails.setId(1L);
            userDetails.setUsername(username);
            userDetails.setStatus(1);
            userDetails.setPassword(adminPassword);

            // 拥有所有的权限
            userDetails.setPermissions(permissionServiceImpl.findList(PageQuery.NonPaged(), PermissionDto.class).getData());

            return userDetails;
        } else {
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
    
}
