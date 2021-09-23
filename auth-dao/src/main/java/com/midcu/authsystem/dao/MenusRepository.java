package com.midcu.authsystem.dao;

import java.util.List;

import com.midcu.authsystem.entity.Menu;

import org.springframework.data.domain.Sort;

public interface MenusRepository extends BaseRepository<Menu, Long>{

    List<Menu> findAllByPid(Long pid, Sort sort);

    List<Menu> findByIdIn(List<Long> ids, Sort sort);

    public <T> List<T> findByIdIn(List<Long> roleIds, Class<T> menuVoClass);

    public <T> List<T> findByIdIn(List<Long> roleIds, Class<T> menuVoClass, Sort sort);

    public <T> List<T> findByPid(Long pid, Class<T> menuVoClass);
    
}
