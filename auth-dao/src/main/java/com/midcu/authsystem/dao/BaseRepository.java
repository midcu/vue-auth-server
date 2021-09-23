package com.midcu.authsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public abstract interface BaseRepository<T, ID> extends JpaRepository<T, ID>{

    <T2> T2 findById(Long id, Class<T2> t2Class);

}
