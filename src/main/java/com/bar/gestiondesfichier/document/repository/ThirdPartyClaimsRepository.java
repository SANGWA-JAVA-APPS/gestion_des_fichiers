package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.ThirdPartyClaims;
import com.bar.gestiondesfichier.document.projection.ThirdPartyClaimsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThirdPartyClaimsRepository extends JpaRepository<ThirdPartyClaims, Long> {

    Page<ThirdPartyClaims> findByActiveTrue(Pageable pageable);
    Page<ThirdPartyClaims> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<ThirdPartyClaims> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    
    @Query("SELECT t FROM ThirdPartyClaims t WHERE t.active = true AND " +
           "(LOWER(t.claimNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.thirdPartyName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ThirdPartyClaims> findByActiveTrueAndClaimNumberOrThirdPartyNameContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT t.id as id, t.dateTime as dateTime, t.claimNumber as claimNumber, " +
           "t.thirdPartyName as thirdPartyName, t.incidentDescription as incidentDescription, " +
           "t.claimAmount as claimAmount, t.liabilityAssessment as liabilityAssessment, " +
           "t.settlementAmount as settlementAmount, t.incidentDate as incidentDate, " +
           "t.document as document, t.status as status, t.doneBy as doneBy " +
           "FROM ThirdPartyClaims t WHERE t.active = true")
    Page<ThirdPartyClaimsProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<ThirdPartyClaims> findByIdAndActiveTrue(Long id);
    boolean existsByClaimNumberAndActiveTrue(String claimNumber);
}