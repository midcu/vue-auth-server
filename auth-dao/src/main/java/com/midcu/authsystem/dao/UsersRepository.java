package com.midcu.authsystem.dao;

import com.midcu.authsystem.entity.User;

public interface UsersRepository extends BaseRepository<User, Long>{
    
    <T> T findByUsername(String username, Class<T> classt);

}
