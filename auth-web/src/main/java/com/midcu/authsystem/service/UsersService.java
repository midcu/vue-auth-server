package com.midcu.authsystem.service;

import java.util.List;

import com.midcu.authsystem.web.qo.PageQuery;
import com.midcu.authsystem.web.rp.ListResponse;
import com.midcu.authsystem.web.vo.UserVo;

public interface UsersService extends BaseService {

    public <T> T findUserByUsername(String username, Class<T> tClass);

    public <T> T findById(Long id, Class<T> tClass);

    public void setUserRoles(Long userId, List<Long> roleIds);

    public ListResponse<UserVo> findUserList(PageQuery query);

}
