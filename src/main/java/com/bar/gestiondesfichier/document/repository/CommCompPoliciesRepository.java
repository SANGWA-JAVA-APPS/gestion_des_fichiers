package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.CommCompPolicies;
import com.bar.gestiondesfichier.document.projection.CommCompPoliciesProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommCompPoliciesRepository extends JpaRepository<CommCompPolicies, Long> {

    Page<CommCompPolicies> findByActiveTrue(Pageable pageable);
    Page<CommCompPolicies> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<CommCompPolicies> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    Page<CommCompPolicies> findByActiveTrueAndSection_Id(Long sectionId, Pageable pageable);
    
    @Query("SELECT p FROM CommCompPolicies p WHERE p.active = true AND " +
           "(LOWER(p.policyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.requirement) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CommCompPolicies> findByActiveTrueAndPolicyNameOrRequirementContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM CommCompPolicies p WHERE p.active = true AND " +
           "(LOWER(p.reference) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CommCompPolicies> findByActiveTrueAndSearchTerms(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p.id as id, p.dateTime as dateTime, p.policyName as policyName, " +
           "p.requirement as requirement, p.complianceLevel as complianceLevel, " +
           "p.effectiveDate as effectiveDate, p.reviewDate as reviewDate, " +
           "p.document as document, p.status as status, p.doneBy as doneBy " +
           "FROM CommCompPolicies p WHERE p.active = true")
    Page<CommCompPoliciesProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<CommCompPolicies> findByIdAndActiveTrue(Long id);
    Optional<CommCompPolicies> findByReferenceAndActiveTrue(String reference);
}