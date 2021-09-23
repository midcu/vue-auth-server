package com.midcu.authsystem.service;

import java.util.List;

public interface PermissionService extends BaseService {
    
    public <T> List<T> findPermissionByRoleId(Long roleId, Class<T> tClass);

    public <T> List<T> findPermissionByUserId(Long userId, Class<T> tClass);

}
