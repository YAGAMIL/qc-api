package com.quantumtime.qc.common.service.impl;

import com.quantumtime.qc.common.entity.BaseEntity;
import com.quantumtime.qc.common.exception.JpaCrudException;
import com.quantumtime.qc.common.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Description: JPA_ServiceImpl基类
 * Created on 2019/10/11 14:43
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
public class BaseServiceImpl<T extends BaseEntity, ID, R extends JpaRepository> implements IBaseService<T, ID> {

    @Autowired
    protected R baseRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable page) {
        log.debug("findAll PageNumber: {} ---> PageSize: {}", page.getPageNumber(), page.getPageSize());
        return baseRepository.findAll(page);
    }

    @Override
    public T findById(ID id) {
        log.debug("findById: {}", id);
        Optional byId = baseRepository.findById(id);
        return byId.isPresent() ? (T) byId.get() : null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public T save(T entity) throws JpaCrudException {
        Optional.ofNullable(entity).orElseThrow(() -> new JpaCrudException("You cannot save an empty entity class."));
        Date createTime = entity.getCreateTime();
        entity.setCreateTime(createTime == null ? new Date() : createTime);
        entity.setUpTime(new Date());
        log.debug("save: {}", entity);
        return (T) baseRepository.saveAndFlush(entity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public T update(T entity, Boolean ...isUpdateTime) throws JpaCrudException {
        Optional.ofNullable(entity).orElseThrow(() -> new JpaCrudException("You cannot update an empty entity class."));
        if (isUpdateTime == null || isUpdateTime.length != 1 || isUpdateTime[0]) {
            entity.setUpTime(new Date());
        }
        return updateForHql(entity);
//        return (T) baseRepository.saveAndFlush(entity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteById(ID id) throws JpaCrudException {
        if (id == null || !baseRepository.existsById(id)) {
            throw new JpaCrudException("Unable to delete data whose ID does not exist");
        }
        log.debug("deleteById: {}", id);
        baseRepository.deleteById(id);
    }

    @Override
    public boolean existsById(ID var1) {
        log.debug("existsById: {}", var1);
        return baseRepository.existsById(var1);
    }

    private T updateForHql(T entity){
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append(" update ");
        sb.append(entity.getClass().getSimpleName());
        sb.append(" set ");
        List<Object> paraments = new ArrayList<>();
        Field primaryKey = null;
        try {
            for(Field f : fields){
                f.setAccessible(true);
                if(f.getAnnotation(Id.class) == null && f.getAnnotation(Column.class) != null && f.get(entity) != null){
                    paraments.add(f.get(entity));
                    if(paraments.size() == 1) {
                        sb.append(" ").append(f.getName()).append(" = ?").append(paraments.size());
                    }else{
                        sb.append(",").append(" ").append(f.getName()).append(" = ?").append(paraments.size());
                    }
                }else if(f.getAnnotation(Id.class) != null && f.get(entity) != null){
                    primaryKey = f;
                }
            }
            Optional.ofNullable(primaryKey).orElseThrow(() -> new JpaCrudException("主键不能为空"));
            if(paraments.size() == 0){
                throw new JpaCrudException("更新字段不能为空");
            }
            sb.append(" where ").append(primaryKey.getName()).append(" = ").append(" ?").append(paraments.size()+1);
            Query query = em.createQuery(sb.toString());
            IntStream.range(0, paraments.size()).forEachOrdered(i -> query.setParameter(i + 1, paraments.get(i)));
            query.setParameter(paraments.size() + 1, primaryKey.get(entity));
            return this.findById((ID) primaryKey.get(entity));
        } catch (IllegalAccessException e) {
            throw new JpaCrudException("更新异常", e);
        }
    }

}
