package com.quantumtime.qc.common.service;

import com.quantumtime.qc.common.exception.JpaCrudException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface IBaseService<T, ID> {

    List<T> findAll();

    List<T> findAll(Sort sort);

    Page<T> findAll(Pageable page);

    T findById(ID id);


    T save(T entity) throws JpaCrudException;

    T update(T entity, Boolean... isUpdateTime) throws JpaCrudException;

    void deleteById(ID id) throws JpaCrudException;

    boolean existsById(ID var1);

}
