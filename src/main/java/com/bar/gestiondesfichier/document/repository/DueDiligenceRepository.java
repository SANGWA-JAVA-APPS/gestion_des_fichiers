package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.DueDiligence;
import com.bar.gestiondesfichier.document.projection.DueDiligenceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DueDiligenceRepository extends JpaRepository<DueDiligence, Long> {

    Page<DueDiligence> findByActiveTrue(Pageable pageable);
    Page<DueDiligence> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<DueDiligence> findByActiveTrueAndSection_Id(Long sectionId, Pageable pageable);
    Page<DueDiligence> findByActiveTrueAndAuditor(String auditor, Pageable pageable);
    
    @Query("SELECT d FROM DueDiligence d WHERE d.active = true AND " +
           "(LOWER(d.projectName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.scope) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<DueDiligence> findByActiveTrueAndProjectNameOrScopeContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT d FROM DueDiligence d WHERE d.active = true AND " +
           "(LOWER(d.reference) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.auditor) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<DueDiligence> findByActiveTrueAndSearchTerms(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT d.id as id, d.dateTime as dateTime, d.projectName as projectName, " +
           "d.entityName as entityName, d.scope as scope, d.findingsReport as findingsReport, " +
           "d.recommendations as recommendations, d.completionDate as completionDate, " +
           "d.document as document, d.status as status, d.doneBy as doneBy " +
           "FROM DueDiligence d WHERE d.active = true")
    Page<DueDiligenceProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<DueDiligence> findByIdAndActiveTrue(Long id);
}