package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.CertLicenses;
import com.bar.gestiondesfichier.document.projection.CertLicensesProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertLicensesRepository extends JpaRepository<CertLicenses, Long> {

    Page<CertLicenses> findByActiveTrue(Pageable pageable);
    Page<CertLicenses> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    
    @Query("SELECT c FROM CertLicenses c WHERE c.active = true AND " +
           "(LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.agentCertifica) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CertLicenses> findByActiveTrueAndSearchTerms(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT c.id as id, c.dateTime as dateTime, c.description as description, " +
           "c.agentCertifica as agentCertifica, c.numeroAgent as numeroAgent, " +
           "c.dateCertificate as dateCertificate, c.dureeCertificat as dureeCertificat, " +
           "c.document as document, c.status as status, c.doneBy as doneBy " +
           "FROM CertLicenses c WHERE c.active = true")
    Page<CertLicensesProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<CertLicenses> findByIdAndActiveTrue(Long id);
}