package com.bar.gestiondesfichier.common.repository;

import com.bar.gestiondesfichier.common.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    List<T> findByActiveTrue();
    
    List<T> findByActiveFalse();
    
    Optional<T> findByIdAndActiveTrue(Long id);
    
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.active = true")
    long countActiveEntities();
    
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.active = false")
    long countInactiveEntities();
}