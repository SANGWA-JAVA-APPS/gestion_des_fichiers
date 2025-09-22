package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.DocStatus;
import com.bar.gestiondesfichier.document.projection.DocStatusProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for DocStatus with pagination and projection support
 */
@Repository
public interface DocStatusRepository extends JpaRepository<DocStatus, Long> {

    // Pagination methods
    Page<DocStatus> findByActiveTrue(Pageable pageable);
    
    // Projection methods
    @Query("SELECT d.id as id, d.name as name, d.description as description, d.active as active " +
           "FROM DocStatus d WHERE d.active = true")
    Page<DocStatusProjection> findAllActiveProjections(Pageable pageable);
    
    // Specific queries
    Optional<DocStatus> findByIdAndActiveTrue(Long id);
    Optional<DocStatus> findByNameAndActiveTrue(String name);
    List<DocStatus> findByActiveTrueOrderByName();
    boolean existsByNameAndActiveTrue(String name);
}