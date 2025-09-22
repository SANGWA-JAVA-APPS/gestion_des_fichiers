package com.bar.gestiondesfichier.common.service;

import com.bar.gestiondesfichier.common.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity> {
    
    List<T> findAll();
    
    List<T> findAllActive();
    
    Optional<T> findById(Long id);
    
    Optional<T> findByIdAndActive(Long id);
    
    T save(T entity);
    
    T update(T entity);
    
    void deleteById(Long id);
    
    void softDeleteById(Long id);
    
    void restoreById(Long id);
    
    long countAll();
    
    long countActive();
    
    long countInactive();
}