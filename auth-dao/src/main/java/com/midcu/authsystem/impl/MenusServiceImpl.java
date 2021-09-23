package com.midcu.authsystem.impl;

import java.util.ArrayList;
import java.util.List;

import com.midcu.authsystem.dao.MenusRepository;
import com.midcu.authsystem.dao.RoleMenuRepository;
import com.midcu.authsystem.dao.UserRoleRepository;
import com.midcu.authsystem.entity.Menu;
import com.midcu.authsystem.entity.RoleMenu;
import com.midcu.authsystem.entity.UserRole;
import com.midcu.authsystem.web.vo.MenuVo;
import com.midcu.authsystem.service.MenusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

@Service
public class MenusServiceImpl extends BaseServiceImpl<Menu> implements MenusService{


    @Autowired
    public MenusRepository menusRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    public RoleMenuRepository roleMenuRepository;

    @Override
    public void deleteMenusById(Long id) {
        List<Long> ids = findByPid(id);
        ids.add(id);
        menusRepository.deleteAllByIdInBatch(ids);
    }

    public List<Long> findByPid(Long pid) {
        List<MenuVo> menuVos = menusRepository.findByPid(pid, MenuVo.class);

        List<Long> list = new ArrayList<Long>();

        for(MenuVo mv: menuVos) {
            list.add(mv.getId());
            list.addAll(findByPid(mv.getId()));
        }

        return list;
    }

    @Override
    public <T> List<T> findMenuByUserId(Long userId, Class<T> tClass) {

        List<UserRole> userRole = userRoleRepository.findByUserId(userId);

        List<Long> roleIds = new ArrayList<Long>();

        for(UserRole ur : userRole) {
            roleIds.add(ur.getRoleId());
        }

        List<RoleMenu> roleMenu = roleMenuRepository.findByRoleIdIn(roleIds);

        List<Long> menuIds = new ArrayList<Long>();
        for(RoleMenu rm : roleMenu) {
            menuIds.add(rm.getMenuId());
        }

        return menusRepository.findByIdIn(menuIds, tClass ,Sort.by("sort"));
    }
    
}
