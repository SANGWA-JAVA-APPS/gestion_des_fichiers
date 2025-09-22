package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.SectionCategory;
import com.bar.gestiondesfichier.document.projection.SectionCategoryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SectionCategory with pagination and projection support
 */
@Repository
public interface SectionCategoryRepository extends JpaRepository<SectionCategory, Long> {

    // Pagination methods
    Page<SectionCategory> findByActiveTrue(Pageable pageable);
    
    // Projection methods
    @Query("SELECT s.id as id, s.name as name, s.description as description, s.active as active " +
           "FROM SectionCategory s WHERE s.active = true")
    Page<SectionCategoryProjection> findAllActiveProjections(Pageable pageable);
    
    // Specific queries
    Optional<SectionCategory> findByIdAndActiveTrue(Long id);
    Optional<SectionCategory> findByNameAndActiveTrue(String name);
    List<SectionCategory> findByActiveTrueOrderByName();
    boolean existsByNameAndActiveTrue(String name);
}