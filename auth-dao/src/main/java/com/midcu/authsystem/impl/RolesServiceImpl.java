package com.midcu.authsystem.impl;

import java.util.ArrayList;
import java.util.List;

import com.midcu.authsystem.dao.MenusRepository;
import com.midcu.authsystem.dao.PermissionRepository;
import com.midcu.authsystem.dao.RoleMenuRepository;
import com.midcu.authsystem.dao.RolePermissionRepository;
import com.midcu.authsystem.dao.RolesRepository;
import com.midcu.authsystem.dao.UserRoleRepository;
import com.midcu.authsystem.entity.Role;
import com.midcu.authsystem.entity.RoleMenu;
import com.midcu.authsystem.entity.RolePermission;
import com.midcu.authsystem.entity.UserRole;
import com.midcu.authsystem.service.RolesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesServiceImpl extends BaseServiceImpl<Role> implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    public MenusRepository menusRepository;

    @Autowired
    public RoleMenuRepository roleMenuRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Override
    public <T> List<T> findRoleByUserId(Long userId, Class<T> tClass) {
        
        List<UserRole> userRole = userRoleRepository.findByUserId(userId);

        List<Long> roleIds = new ArrayList<Long>();

        for(UserRole ur : userRole) {
            roleIds.add(ur.getRoleId());
        }

        return rolesRepository.findByIdIn(roleIds, tClass);
    }

    
    @Override
    public <T> List<T> findMenuVoByRoleId(Long roleId, Class<T> tClass) {
        List<RoleMenu> roleMenu = roleMenuRepository.findByRoleId(roleId);

        List<Long> menuIds = new ArrayList<Long>();
        for(RoleMenu rm : roleMenu) {
            menuIds.add(rm.getMenuId());
        }
        
        return menusRepository.findByIdIn(menuIds, tClass);
    }

    @Override
    public void deleteRoleById(Long id) {

        rolesRepository.deleteById(id);

        roleMenuRepository.deleteByRoleId(id);

        rolePermissionRepository.deleteByRoleId(id);

        userRoleRepository.deleteByRoleId(id);
    }


    @Override
    public void setRoleMenus(Long roleId, List<Long> menuIds) {

        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);

        List<Long> roleMenuIds = new ArrayList<Long>();

        for(RoleMenu rm : roleMenus) {
            roleMenuIds.add(rm.getId());
        }

        roleMenuRepository.deleteAllById(roleMenuIds);

        roleMenus = new ArrayList<RoleMenu>();

        RoleMenu roleMenu;

        for(Long menuId : menuIds) {
            roleMenu = new RoleMenu();

            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);

            roleMenus.add(roleMenu);
        }

        roleMenuRepository.saveAll(roleMenus);
        
    }


    @Override
    public void setRolePermissions(Long roleId, List<Long> permissionIds) {
        
        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleId(roleId);

        List<Long>  rolePermissionIds = new ArrayList<Long>();

        for(RolePermission rp : rolePermissions) {
            rolePermissionIds.add(rp.getId());
        }

        rolePermissionRepository.deleteAllById(rolePermissionIds);

        rolePermissions = new ArrayList<RolePermission>();

        RolePermission rolePermission;

        for(Long permissionId : permissionIds) {
            rolePermission = new RolePermission();

            rolePermission.setPermissionId(permissionId);;
            rolePermission.setRoleId(roleId);

            rolePermissions.add(rolePermission);
        }

        rolePermissionRepository.saveAll(rolePermissions);
        
    }
    
}
