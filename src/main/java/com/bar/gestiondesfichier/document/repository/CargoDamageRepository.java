package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.CargoDamage;
import com.bar.gestiondesfichier.document.projection.CargoDamageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CargoDamageRepository extends JpaRepository<CargoDamage, Long> {

    Page<CargoDamage> findByActiveTrue(Pageable pageable);
    Page<CargoDamage> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<CargoDamage> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    
    @Query("SELECT c FROM CargoDamage c WHERE c.active = true AND " +
           "(LOWER(c.claimNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.damageDescription) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CargoDamage> findByActiveTrueAndClaimNumberOrDamageDescriptionContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT c.id as id, c.dateTime as dateTime, c.claimNumber as claimNumber, " +
           "c.cargoType as cargoType, c.damageDescription as damageDescription, " +
           "c.estimatedValue as estimatedValue, c.incidentDate as incidentDate, " +
           "c.document as document, c.status as status, c.doneBy as doneBy " +
           "FROM CargoDamage c WHERE c.active = true")
    Page<CargoDamageProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<CargoDamage> findByIdAndActiveTrue(Long id);
    boolean existsByClaimNumberAndActiveTrue(String claimNumber);
}