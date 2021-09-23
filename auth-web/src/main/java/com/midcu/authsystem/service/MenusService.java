package com.midcu.authsystem.service;

import java.util.List;

public interface MenusService extends BaseService {

    public void deleteMenusById(Long id);

    public <T> List<T> findMenuByUserId(Long userId, Class<T> tClass);
    
}
