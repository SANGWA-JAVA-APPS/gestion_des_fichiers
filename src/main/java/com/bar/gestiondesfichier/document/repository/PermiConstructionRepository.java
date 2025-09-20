package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.PermiConstruction;
import com.bar.gestiondesfichier.document.projection.PermiConstructionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for PermiConstruction with pagination and projection support
 */
@Repository
public interface PermiConstructionRepository extends JpaRepository<PermiConstruction, Long> {

    Page<PermiConstruction> findByActiveTrue(Pageable pageable);
    Page<PermiConstruction> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<PermiConstruction> findByActiveTrueAndSectionCategory_Id(Long sectionCategoryId, Pageable pageable);
    Page<PermiConstruction> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    
    @Query("SELECT p FROM PermiConstruction p WHERE p.active = true AND " +
           "(LOWER(p.numeroPermis) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.projet) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<PermiConstruction> findByActiveTrueAndNumeroPermisOrProjetContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM PermiConstruction p WHERE p.active = true AND " +
           "LOWER(p.referenceTitreFoncier) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.refPermisConstuire) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PermiConstruction> findByActiveTrueAndReferenceContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p.id as id, p.dateTime as dateTime, p.numeroPermis as numeroPermis, " +
           "p.projet as projet, p.localisation as localisation, p.dateDelivrance as dateDelivrance, " +
           "p.dateExpiration as dateExpiration, p.autoriteDelivrance as autoriteDelivrance, " +
           "p.referenceTitreFoncier as referenceTitreFoncier, p.refPermisConstuire as refPermisConstuire, " +
           "p.dateValidation as dateValidation, p.dateEstimeeTravaux as dateEstimeeTravaux, " +
           "p.document as document, p.status as status, p.doneBy as doneBy, p.sectionCategory as sectionCategory " +
           "FROM PermiConstruction p WHERE p.active = true")
    Page<PermiConstructionProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<PermiConstruction> findByIdAndActiveTrue(Long id);
    Optional<PermiConstruction> findByNumeroPermisAndActiveTrue(String numeroPermis);
    Optional<PermiConstruction> findByReferenceTitreFoncierAndActiveTrue(String referenceTitreFoncier);
    boolean existsByNumeroPermisAndActiveTrue(String numeroPermis);
    boolean existsByReferenceTitreFoncierAndActiveTrue(String referenceTitreFoncier);
}