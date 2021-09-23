package com.midcu.authsystem.dao;

import java.util.List;

import com.midcu.authsystem.entity.RoleMenu;

import org.springframework.data.repository.CrudRepository;

public interface RoleMenuRepository extends CrudRepository<RoleMenu, Long>{
    
    public List<RoleMenu> findByRoleIdIn(List<Long> roleIds);

    public List<RoleMenu> findByRoleId(Long roleId);

    public void deleteByRoleId(Long roleId);
}
