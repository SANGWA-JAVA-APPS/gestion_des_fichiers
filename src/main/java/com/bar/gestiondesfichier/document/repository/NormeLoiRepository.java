package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.NormeLoi;
import com.bar.gestiondesfichier.document.projection.NormeLoiProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for NormeLoi with pagination and projection support
 */
@Repository
public interface NormeLoiRepository extends JpaRepository<NormeLoi, Long> {

    // Pagination methods
    Page<NormeLoi> findByActiveTrue(Pageable pageable);
    Page<NormeLoi> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<NormeLoi> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    
    @Query("SELECT n FROM NormeLoi n WHERE n.active = true AND " +
           "LOWER(n.reference) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(n.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<NormeLoi> findByActiveTrueAndReferenceOrDescriptionContaining(@Param("search") String search, Pageable pageable);
    
    // Projection methods
    @Query("SELECT n.id as id, n.dateTime as dateTime, n.reference as reference, " +
           "n.description as description, n.dateVigueur as dateVigueur, " +
           "n.domaineApplication as domaineApplication, " +
           "n.document as document, n.status as status, n.doneBy as doneBy " +
           "FROM NormeLoi n WHERE n.active = true")
    Page<NormeLoiProjection> findAllActiveProjections(Pageable pageable);
    
    // Specific queries
    Optional<NormeLoi> findByIdAndActiveTrue(Long id);
    Optional<NormeLoi> findByReferenceAndActiveTrue(String reference);
    boolean existsByReferenceAndActiveTrue(String reference);
}