package com.midcu.authsystem.dao;

import java.util.List;

import com.midcu.authsystem.entity.UserRole;

import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Long>{

    public List<UserRole> findByUserId(Long userId);

    public List<UserRole> findByUserIdIn(List<Long> userIds);

    public void deleteByRoleId(Long roleId);
}
