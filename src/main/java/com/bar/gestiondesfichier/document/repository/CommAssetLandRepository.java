package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.CommAssetLand;
import com.bar.gestiondesfichier.document.projection.CommAssetLandProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CommAssetLand with pagination and projection support
 */
@Repository
public interface CommAssetLandRepository extends JpaRepository<CommAssetLand, Long> {

    Page<CommAssetLand> findByActiveTrue(Pageable pageable);
    Page<CommAssetLand> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<CommAssetLand> findByActiveTrueAndSection_Id(Long sectionId, Pageable pageable);
    
    @Query("SELECT c FROM CommAssetLand c WHERE c.active = true AND " +
           "(LOWER(c.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.location) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CommAssetLand> findByActiveTrueAndAssetNameOrLocationContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT c FROM CommAssetLand c WHERE c.active = true AND " +
           "LOWER(c.reference) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CommAssetLand> findByActiveTrueAndReferenceContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT c.id as id, c.dateTime as dateTime, c.description as description, " +
           "c.reference as reference, c.dateObtention as dateObtention, " +
           "c.coordonneesGps as coordonneesGps, c.emplacement as emplacement, " +
           "c.document as document, c.status as status, c.doneBy as doneBy, c.section as section " +
           "FROM CommAssetLand c WHERE c.active = true")
    Page<CommAssetLandProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<CommAssetLand> findByIdAndActiveTrue(Long id);
    Optional<CommAssetLand> findByReferenceAndActiveTrue(String reference);
    boolean existsByReferenceAndActiveTrue(String reference);
}