package com.midcu.authsystem.dao;

import java.util.List;

import com.midcu.authsystem.entity.Permission;

public interface PermissionRepository extends BaseRepository<Permission, Long>{
    
    public List<Permission> findByIdIn(List<Long> permissionIds);

    public <T> List<T> findByIdIn(List<Long> permissionIds, Class<T> classt);

}
