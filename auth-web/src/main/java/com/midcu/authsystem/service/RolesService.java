package com.midcu.authsystem.service;

import java.util.List;

public interface RolesService extends BaseService {

    public <T> List<T> findRoleByUserId(Long userId, Class<T> tClass);

    public <T> List<T> findMenuVoByRoleId(Long roleId, Class<T> tClass);

    public void deleteRoleById(Long id);

    public void setRoleMenus(Long roleId, List<Long> menuIds);

    public void setRolePermissions(Long roleId, List<Long> permissionIds);
    
}
