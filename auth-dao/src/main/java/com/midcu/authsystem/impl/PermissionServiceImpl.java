package com.midcu.authsystem.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import com.midcu.authsystem.dao.PermissionRepository;
import com.midcu.authsystem.dao.RolePermissionRepository;
import com.midcu.authsystem.dao.UserRoleRepository;
import com.midcu.authsystem.entity.Permission;
import com.midcu.authsystem.entity.RolePermission;
import com.midcu.authsystem.entity.UserRole;
import com.midcu.authsystem.exception.SysException;
import com.midcu.authsystem.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public <T> List<T> findPermissionByRoleId(Long roleId, Class<T> tClass) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> cq = builder.createQuery(tClass);

        Root<Permission> root = cq.from(Permission.class);

        Field[] roFields = tClass.getDeclaredFields();

        // permission id查询  查询表 role_permission

        Subquery<Long> cqRolePermission = cq.subquery(Long.class);
        Root<RolePermission> rootRolePermission = cqRolePermission.from(RolePermission.class);

        cqRolePermission.select(rootRolePermission.get("permissionId")).where(builder.equal(rootRolePermission.get("roleId"), roleId));

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

        cq.where(builder.in(root.get("id")).value(cqRolePermission));

        return entityManager.createQuery(cq).getResultList();

    }

    @Override
    public <T> List<T> findPermissionByUserId(Long userId, Class<T> tClass) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> cq = builder.createQuery(tClass);

        Root<Permission> root = cq.from(Permission.class);

        Field[] roFields = tClass.getDeclaredFields();

        // permission id查询  查询表 role_permission

        Subquery<Long> cqRolePermission = cq.subquery(Long.class);
        Root<RolePermission> rootRolePermission = cqRolePermission.from(RolePermission.class);

        // role id 查询 查询表 user_role

        Subquery<Long> cqUserRole = cqRolePermission.subquery(Long.class);
        Root<UserRole> rootUserRole = cqUserRole.from(UserRole.class);

        cqUserRole.select(rootUserRole.get("roleId")).where(builder.equal(rootUserRole.get("userId"), userId));

        cqRolePermission.select(rootRolePermission.get("permissionId")).where(builder.in(rootRolePermission.get("roleId")).value(cqUserRole));

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

        cq.where(builder.in(root.get("id")).value(cqRolePermission), builder.equal(root.get("state"), 1));

        return entityManager.createQuery(cq).getResultList();
    }
    
}
