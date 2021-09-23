package com.midcu.authsystem.dao;

import java.util.List;

import com.midcu.authsystem.entity.Role;

import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends BaseRepository<Role, Long>{
    
    Role findById(long id);

    List<Role> findAllByName(String name);

    List<Role> findByIdIn(List<Long> roleIds);

    public <T> List<T> findByIdIn(List<Long> roleIds, Class<T> roleVoClass);

}
