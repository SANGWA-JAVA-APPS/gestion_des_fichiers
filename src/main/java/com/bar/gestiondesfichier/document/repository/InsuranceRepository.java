package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.Insurance;
import com.bar.gestiondesfichier.document.projection.InsuranceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    Page<Insurance> findByActiveTrue(Pageable pageable);
    Page<Insurance> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<Insurance> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    
    @Query("SELECT i FROM Insurance i WHERE i.active = true AND " +
           "(LOWER(i.policyNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.insuranceType) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Insurance> findByActiveTrueAndPolicyNumberOrInsuranceTypeContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT i.id as id, i.dateTime as dateTime, i.policyNumber as policyNumber, " +
           "i.insuranceType as insuranceType, i.provider as provider, " +
           "i.coverageAmount as coverageAmount, i.premium as premium, " +
           "i.effectiveDate as effectiveDate, i.expiryDate as expiryDate, " +
           "i.document as document, i.status as status, i.doneBy as doneBy " +
           "FROM Insurance i WHERE i.active = true")
    Page<InsuranceProjection> findAllActiveProjections(Pageable pageable);
    
    @Query("SELECT i FROM Insurance i WHERE i.active = true AND " +
           "i.expiryDate <= FUNCTION('DATE_ADD', CURRENT_DATE(), :days, 'DAY')")
    Page<Insurance> findExpiringWithinDays(@Param("days") Integer days, Pageable pageable);
    
    Optional<Insurance> findByIdAndActiveTrue(Long id);
    boolean existsByPolicyNumberAndActiveTrue(String policyNumber);
}