package com.midcu.authsystem.impl;

import java.util.ArrayList;
import java.util.List;

import com.midcu.authsystem.dao.PermissionRepository;
import com.midcu.authsystem.dao.RolePermissionRepository;
import com.midcu.authsystem.dao.UserRoleRepository;
import com.midcu.authsystem.entity.Permission;
import com.midcu.authsystem.entity.RolePermission;
import com.midcu.authsystem.entity.UserRole;
import com.midcu.authsystem.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public <T> List<T> findPermissionByRoleId(Long roleId, Class<T> tClass) {

        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleId(roleId);

        List<Long> permissionIds = new ArrayList<Long>();

        for(RolePermission rp : rolePermissions) {
            permissionIds.add(rp.getPermissionId());
        }

        return permissionRepository.findByIdIn(permissionIds, tClass);

    }

    @Override
    public <T> List<T> findPermissionByUserId(Long userId, Class<T> tClass) {

        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);

        List<Long> roleIds = new ArrayList<Long>();


        for(UserRole ur : userRoles) {
            roleIds.add(ur.getRoleId());
        }

        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleIdIn(roleIds);

        List<Long> permissionIds = new ArrayList<Long>();

        for(RolePermission rp : rolePermissions) {
            permissionIds.add(rp.getPermissionId());
        }

        return permissionRepository.findByIdIn(permissionIds, tClass);
    }
    
}
