package com.midcu.authsystem.service;

import com.midcu.authsystem.web.qo.PageQuery;
import com.midcu.authsystem.web.rp.ListResponse;
import com.midcu.authsystem.web.ro.BaseRo;

public interface BaseService {

    public <T1> T1 save(BaseRo ro, Class<T1> t1Class);

    public void update(BaseRo ro, Long id);

    public <T2> T2 findById(Long id, Class<T2> t2Class);

    public void deleteById(Long id);

    public <T4 extends Object> ListResponse<T4> findList(PageQuery query, Class<T4> t4Class);
}
