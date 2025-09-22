package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.CommFollowupAudit;
import com.bar.gestiondesfichier.document.projection.CommFollowupAuditProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommFollowupAuditRepository extends JpaRepository<CommFollowupAudit, Long> {

    Page<CommFollowupAudit> findByActiveTrue(Pageable pageable);
    Page<CommFollowupAudit> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<CommFollowupAudit> findByActiveTrueAndSection_Id(Long sectionId, Pageable pageable);
    Page<CommFollowupAudit> findByActiveTrueAndAuditor(String auditor, Pageable pageable);
    
    @Query("SELECT a FROM CommFollowupAudit a WHERE a.active = true AND " +
           "(LOWER(a.reference) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.auditor) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CommFollowupAudit> findByActiveTrueAndSearchTerms(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT a FROM CommFollowupAudit a WHERE a.active = true AND " +
           "(LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.reference) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CommFollowupAudit> findByActiveTrueAndAuditTitleOrFindingsContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT a.id as id, a.dateTime as dateTime, a.reference as reference, " +
           "a.description as description, a.dateAudit as dateAudit, a.auditor as auditor, " +
           "a.numNonConform as numNonConform, a.typeConform as typeConform, " +
           "a.percentComplete as percentComplete, a.docAttach as docAttach, " +
           "a.document as document, a.status as status, a.doneBy as doneBy, a.section as section " +
           "FROM CommFollowupAudit a WHERE a.active = true")
    Page<CommFollowupAuditProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<CommFollowupAudit> findByIdAndActiveTrue(Long id);
}