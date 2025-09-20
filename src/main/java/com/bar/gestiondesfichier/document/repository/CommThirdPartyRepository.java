package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.CommThirdParty;
import com.bar.gestiondesfichier.document.projection.CommThirdPartyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommThirdPartyRepository extends JpaRepository<CommThirdParty, Long> {

    Page<CommThirdParty> findByActiveTrue(Pageable pageable);
    Page<CommThirdParty> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<CommThirdParty> findByActiveTrueAndSection_Id(Long sectionId, Pageable pageable);
    
    @Query("SELECT t FROM CommThirdParty t WHERE t.active = true AND " +
           "(LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.location) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CommThirdParty> findByActiveTrueAndSearchTerms(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT t FROM CommThirdParty t WHERE t.active = true AND " +
           "(LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.activities) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CommThirdParty> findByActiveTrueAndThirdPartyNameOrServiceTypeContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT t.id as id, t.dateTime as dateTime, t.name as name, " +
           "t.location as location, t.validity as validity, t.activities as activities, " +
           "t.document as document, t.status as status, t.doneBy as doneBy, t.section as section " +
           "FROM CommThirdParty t WHERE t.active = true")
    Page<CommThirdPartyProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<CommThirdParty> findByIdAndActiveTrue(Long id);
}