package com.midcu.authsystem.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.transaction.Transactional;

import com.midcu.authsystem.dao.BaseRepository;
import com.midcu.authsystem.exception.SysException;
import com.midcu.authsystem.web.qo.PageQuery;
import com.midcu.authsystem.web.rp.ListResponse;
import com.midcu.authsystem.web.ro.BaseRo;
import com.midcu.authsystem.web.vo.PageVo;
import com.midcu.authsystem.service.BaseService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseServiceImpl<T> implements BaseService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据泛型获取class 根据class获取实例
     */
    private Class<T> tClass;

    @Autowired
    public BaseRepository<T, Long> repository;

    public T getInstance() {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            log.error("实例获取失败！", e);
            throw new SysException("实例获取失败！");
        }
    };

    public <T3> T3 getInstance(Class<T3> t3Class) {
        try {
            return t3Class.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            log.error("实例获取失败！", e);
            throw new SysException("实例获取失败！");
        }
    };

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void initClass() {
        this.tClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseServiceImpl.class);
    };

    @Transactional
    @Override
    public void update(BaseRo ro, Long id) {

        T entity = getInstance();

        BeanUtils.copyProperties(ro, entity);
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaUpdate<T> criteria = builder.createCriteriaUpdate(tClass);

        Root<T> root = criteria.from(tClass);

        Field[] queryFields = entity.getClass().getDeclaredFields();

        try {
            for(Field f: queryFields) {
                f.setAccessible(true);
                if (f.get(entity) != null) {
                    criteria.set(f.getName(), f.get(entity));
                }
            }
        } catch (Exception e) {
            log.error("BaseServiceImpl", e);
            throw new SysException("更新失败！");
        }

        criteria.where(builder.equal(root.get("id"), id));

        if (entityManager.createQuery(criteria).executeUpdate() != 1) {
            throw new SysException("更新失败！");
        }

    }

    @Override
    public <T2> T2 save(BaseRo ro, Class<T2> t2Class) {
        T entity = getInstance();
        
        BeanUtils.copyProperties(ro, entity);

        entity = repository.save(entity);

        if (entity == null) {
            throw new SysException("保存失败！");
        }

        T2 vo = getInstance(t2Class);

        BeanUtils.copyProperties(entity, vo);

        return vo;
    }

    @Override
    public <T2> T2 findById(Long id, Class<T2> t2Class) {

        T2 entity = repository.findById(id, t2Class);

        if (entity == null) {
            throw new SysException("查询失败！未找到对象");
        }
        return entity;
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);

    }

    @Override
    public <T4 extends Object> ListResponse<T4> findList(PageQuery query, Class<T4> t4Class) {
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // 模糊查询
        CriteriaQuery<T4> cq = builder.createQuery(t4Class);

        CriteriaQuery<Long> cqL = builder.createQuery(Long.class);

        Root<T> root = cq.from(tClass);

        Field[] queryFields = query.getClass().getFields();

        Field[] roFields = t4Class.getFields();

        try {
            // 请求体设置为查询条件
            for(Field f: queryFields) {
                f.setAccessible(true);
                if (f.get(query) != null) {

                    cq.where(builder.like(root.get(f.getName()), "%" + f.get(query).toString() + "%"));

                    cqL.where(builder.like(root.get(f.getName()), "%" + f.get(query).toString() + "%"));
                }
            }

            // 请求体设置为需要查询的字段  字段为public类型的参数
            List<Selection<?>> selections = new ArrayList<Selection<?>>();
            for(Field f: roFields) {
                f.setAccessible(true);
                selections.add(root.get(f.getName()));
            }
            cq.multiselect(selections);

            if (query.getSortBy() != null) {
                cq.orderBy(builder.asc(root.get(query.getSortBy())));
            }

        } catch (Exception e) {
            log.error("查询参数问题", e);
            throw new SysException("查询失败！");
        }


        List<T4> list;
        PageVo pageVo = null;

        if (query.getPaged()) {
            list = entityManager.createQuery(cq).setFirstResult((query.getPage() - 1) * query.getPageSize()).setMaxResults(query.getPageSize()).getResultList();
        } else {
            list = entityManager.createQuery(cq).getResultList();
        }
        
        cqL.select(builder.count(cqL.from(tClass)));

        if (query.getPaged()) {
            pageVo = new PageVo(query.getPage(), query.getPageSize(), entityManager.createQuery(cqL).getSingleResult());
        }

        return new ListResponse<T4>(list, pageVo);
    }
}
