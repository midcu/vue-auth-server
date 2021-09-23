package com.midcu.authsystem.dao;

import java.util.List;

import com.midcu.authsystem.entity.RolePermission;

import org.springframework.data.repository.CrudRepository;

public interface RolePermissionRepository extends CrudRepository<RolePermission, Long>{

    public List<RolePermission> findByRoleIdIn(List<Long> roleIds);

    public List<RolePermission> findAllByRoleId(Long roleId);

    public void deleteByRoleId(Long roleId);
    
}
