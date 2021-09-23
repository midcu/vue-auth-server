package com.midcu.authsystem.impl;

import java.util.ArrayList;
import java.util.List;

import com.midcu.authsystem.dao.MenusRepository;
import com.midcu.authsystem.dao.PermissionRepository;
import com.midcu.authsystem.dao.RoleMenuRepository;
import com.midcu.authsystem.dao.RolePermissionRepository;
import com.midcu.authsystem.dao.UserRoleRepository;
import com.midcu.authsystem.dao.UsersRepository;
import com.midcu.authsystem.entity.User;
import com.midcu.authsystem.entity.UserRole;
import com.midcu.authsystem.web.qo.PageQuery;
import com.midcu.authsystem.web.rp.ListResponse;
import com.midcu.authsystem.web.vo.UserVo;
import com.midcu.authsystem.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends BaseServiceImpl<User> implements UsersService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    
    @Autowired
    public MenusRepository menusRepository;

    @Autowired
    public RoleMenuRepository roleMenuRepository;

    @Override
    public void deleteById(Long userId) {
        
        List<UserRole> userRole = userRoleRepository.findByUserId(userId);

        List<Long> userRoleIds = new ArrayList<Long>();

        for(UserRole ur : userRole) {
            userRoleIds.add(ur.getId());
        }

        usersRepository.deleteById(userId);

        userRoleRepository.deleteAllById(userRoleIds);

    }

    @Override
    public void setUserRoles(Long userId, List<Long> roleIds) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);

        List<Long> userRoleIds = new ArrayList<Long>();

        for(UserRole ur : userRoles) {
            userRoleIds.add(ur.getId());
        }

        userRoleRepository.deleteAllById(userRoleIds);

        userRoles = new ArrayList<UserRole>();

        UserRole userRole;

        for(Long roleId : roleIds) {
            userRole = new UserRole();

            userRole.setUserId(userId);
            userRole.setRoleId(roleId);

            userRoles.add(userRole);
        }

        userRoleRepository.saveAll(userRoles);
        
    }

    @Override
    public ListResponse<UserVo> findUserList(PageQuery query) {
        ListResponse<UserVo> userVoList = findList(query, UserVo.class);

        List<Long> userIds = new ArrayList<>();

        for(UserVo userVo: userVoList.getData()) {
            userIds.add(userVo.getId());
        }
        
        List<UserRole> userRoles = userRoleRepository.findByUserIdIn(userIds);

        for(UserVo userVo: userVoList.getData()) {
            userVo.setRoleIds(new ArrayList<>());

            List<Long> roleIds = userVo.getRoleIds();

            for(UserRole userRole:userRoles) {
                if (userRole.getUserId().equals(userVo.getId())){
                    roleIds.add(userRole.getRoleId());
                }
            }
        }

        return userVoList;
    }

    @Override
    public <T> T findUserByUsername(String username, Class<T> tClass) {
        return usersRepository.findByUsername(username, tClass);
    }

    @Override
    public <T> T findById(Long id, Class<T> tClass) {
        return usersRepository.findById(id, tClass);
    }

    // public static void main(String[] args) {
    //     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //     String a = encoder.encode("123");

    //     System.out.println(a);

    // }

    
}
