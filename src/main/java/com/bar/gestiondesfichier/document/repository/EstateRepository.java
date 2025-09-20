package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.Estate;
import com.bar.gestiondesfichier.document.projection.EstateProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Estate with pagination and projection support
 */
@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

    Page<Estate> findByActiveTrue(Pageable pageable);
    Page<Estate> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<Estate> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    Page<Estate> findByActiveTrueAndEstateType(String estateType, Pageable pageable);
    
    @Query("SELECT e FROM Estate e WHERE e.active = true AND " +
           "(LOWER(e.propertyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.location) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Estate> findByActiveTrueAndPropertyNameOrLocationContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT e FROM Estate e WHERE e.active = true AND " +
           "LOWER(e.reference) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.estateType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.emplacement) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Estate> findByActiveTrueAndSearchTerms(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT e.id as id, e.dateTime as dateTime, e.reference as reference, " +
           "e.estateType as estateType, e.emplacement as emplacement, " +
           "e.coordonneesGps as coordonneesGps, e.dateOfBuilding as dateOfBuilding, " +
           "e.comments as comments, " +
           "e.document as document, e.status as status, e.doneBy as doneBy " +
           "FROM Estate e WHERE e.active = true")
    Page<EstateProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<Estate> findByIdAndActiveTrue(Long id);
    Optional<Estate> findByReferenceAndActiveTrue(String reference);
    boolean existsByReferenceAndActiveTrue(String reference);
}