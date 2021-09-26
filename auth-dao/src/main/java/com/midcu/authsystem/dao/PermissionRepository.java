package com.midcu.authsystem.dao;

import java.util.List;

import com.midcu.authsystem.entity.Permission;

import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends BaseRepository<Permission, Long>{
    
    public List<Permission> findByIdIn(List<Long> permissionIds);

    public <T> List<T> findByIdIn(List<Long> permissionIds, Class<T> classt);

    @Query("select name from Permission p where id in ( select permissionId from RolePermission where roleId in ( select roleId from UserRole where userId = ?1) )")
    public <T> List<T> findPermissionByUserId(Long userId);

    @Query("select name from Permission p where id in ( select permissionId from RolePermission where roleId = ?1 )")
    public <T> List<T> findPermissionByRoleId(Long roleId);

    @Query("select name from Permission p where id in ( select permissionId from RolePermission where roleId = ?1 )")
    public <T> List<T> findByRoleId(Long roleId);

}
