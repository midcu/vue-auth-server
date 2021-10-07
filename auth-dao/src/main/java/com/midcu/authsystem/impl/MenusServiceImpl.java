package com.midcu.authsystem.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import com.midcu.authsystem.dao.MenusRepository;
import com.midcu.authsystem.dao.RoleMenuRepository;
import com.midcu.authsystem.dao.UserRoleRepository;
import com.midcu.authsystem.entity.Menu;
import com.midcu.authsystem.entity.RoleMenu;
import com.midcu.authsystem.entity.UserRole;
import com.midcu.authsystem.exception.SysException;
import com.midcu.authsystem.web.vo.MenuVo;
import com.midcu.authsystem.service.MenusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> cq = builder.createQuery(tClass);

        Root<Menu> root = cq.from(Menu.class);

        Field[] roFields = tClass.getDeclaredFields();

        // permission id查询  查询表 role_permission

        Subquery<Long> cqRoleMenu = cq.subquery(Long.class);
        Root<RoleMenu> rootRoleMenu = cqRoleMenu.from(RoleMenu.class);

        // role id 查询 查询表 user_role

        Subquery<Long> cqUserRole = cqRoleMenu.subquery(Long.class);
        Root<UserRole> rootUserRole = cqUserRole.from(UserRole.class);

        cqUserRole.select(rootUserRole.get("roleId")).where(builder.equal(rootUserRole.get("userId"), userId));

        cqRoleMenu.select(rootRoleMenu.get("menuId")).where(builder.in(rootRoleMenu.get("roleId")).value(cqUserRole));

        try {
            // 请求体设置为需要查询的字段  字段为public类型的参数
            List<Selection<?>> selections = new ArrayList<Selection<?>>();
            for(Field f: roFields) {
                f.setAccessible(true);
                selections.add(root.get(f.getName()));
            }
            cq.multiselect(selections);

        } catch (Exception e) {
            log.error("查询参数问题", e);
            throw new SysException("查询失败！");
        }

        cq.where(builder.in(root.get("id")).value(cqRoleMenu)).orderBy(builder.asc(root.get("sort"))).where(builder.equal(root.get("state"), 1));

        return entityManager.createQuery(cq).getResultList();
    }
    
}
