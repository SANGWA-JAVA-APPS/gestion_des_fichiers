package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.AccordConcession;
import com.bar.gestiondesfichier.document.projection.AccordConcessionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for AccordConcession with pagination and projection support
 */
@Repository
public interface AccordConcessionRepository extends JpaRepository<AccordConcession, Long> {

    Page<AccordConcession> findByActiveTrue(Pageable pageable);
    Page<AccordConcession> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<AccordConcession> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    
    @Query("SELECT a FROM AccordConcession a WHERE a.active = true AND " +
           "(LOWER(a.numeroAccord) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.objetConcession) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<AccordConcession> findByActiveTrueAndNumeroAccordOrObjetConcessionContaining(@Param("search") String search, Pageable pageable);

    @Query("SELECT a.id as id, a.dateTime as dateTime, a.contratConcession as contratConcession, " +
           "a.numeroAccord as numeroAccord, a.objetConcession as objetConcession, a.concessionnaire as concessionnaire, " +
           "a.dureeAnnees as dureeAnnees, a.conditionsFinancieres as conditionsFinancieres, " +
           "a.emplacement as emplacement, a.coordonneesGps as coordonneesGps, " +
           "a.rapportTransfertGestion as rapportTransfertGestion, a.dateDebutConcession as dateDebutConcession, " +
           "a.dateFinConcession as dateFinConcession, " +
           "a.document as document, a.status as status, a.doneBy as doneBy, a.sectionCategory as sectionCategory " +
           "FROM AccordConcession a WHERE a.active = true AND " +
           "(LOWER(a.numeroAccord) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.objetConcession) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<AccordConcessionProjection> findByActiveTrueAndNumeroAccordOrObjetConcessionContainingProjections(@Param("search") String search, Pageable pageable);

    @Query("SELECT a.id as id, a.dateTime as dateTime, a.contratConcession as contratConcession, " +
           "a.numeroAccord as numeroAccord, a.objetConcession as objetConcession, a.concessionnaire as concessionnaire, " +
           "a.dureeAnnees as dureeAnnees, a.conditionsFinancieres as conditionsFinancieres, " +
           "a.emplacement as emplacement, a.coordonneesGps as coordonneesGps, " +
           "a.rapportTransfertGestion as rapportTransfertGestion, a.dateDebutConcession as dateDebutConcession, " +
           "a.dateFinConcession as dateFinConcession, " +
           "a.document as document, a.status as status, a.doneBy as doneBy, a.sectionCategory as sectionCategory " +
           "FROM AccordConcession a WHERE a.active = true AND a.status.id = :statusId")
    Page<AccordConcessionProjection> findByActiveTrueAndStatusIdProjections(@Param("statusId") Long statusId, Pageable pageable);

    @Query("SELECT a.id as id, a.dateTime as dateTime, a.contratConcession as contratConcession, " +
           "a.numeroAccord as numeroAccord, a.objetConcession as objetConcession, a.concessionnaire as concessionnaire, " +
           "a.dureeAnnees as dureeAnnees, a.conditionsFinancieres as conditionsFinancieres, " +
           "a.emplacement as emplacement, a.coordonneesGps as coordonneesGps, " +
           "a.rapportTransfertGestion as rapportTransfertGestion, a.dateDebutConcession as dateDebutConcession, " +
           "a.dateFinConcession as dateFinConcession, " +
           "a.document as document, a.status as status, a.doneBy as doneBy, a.sectionCategory as sectionCategory " +
           "FROM AccordConcession a WHERE a.active = true AND a.sectionCategory.id = :sectionCategoryId")
    Page<AccordConcessionProjection> findByActiveTrueAndSectionCategoryIdProjections(@Param("sectionCategoryId") Long sectionCategoryId, Pageable pageable);

    @Query("SELECT a.id as id, a.dateTime as dateTime, a.contratConcession as contratConcession, " +
           "a.numeroAccord as numeroAccord, a.objetConcession as objetConcession, a.concessionnaire as concessionnaire, " +
           "a.dureeAnnees as dureeAnnees, a.conditionsFinancieres as conditionsFinancieres, " +
           "a.emplacement as emplacement, a.coordonneesGps as coordonneesGps, " +
           "a.rapportTransfertGestion as rapportTransfertGestion, a.dateDebutConcession as dateDebutConcession, " +
           "a.dateFinConcession as dateFinConcession, " +
           "a.document as document, a.status as status, a.doneBy as doneBy, a.sectionCategory as sectionCategory " +
           "FROM AccordConcession a WHERE a.active = true AND a.document.id = :documentId")
    Page<AccordConcessionProjection> findByActiveTrueAndDocumentIdProjections(@Param("documentId") Long documentId, Pageable pageable);

    Page<AccordConcession> findByActiveTrueAndSectionCategory_Id(Long sectionCategoryId, Pageable pageable);

    @Query("SELECT a FROM AccordConcession a WHERE a.active = true AND " +
           "LOWER(a.contratConcession) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.emplacement) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<AccordConcession> findByActiveTrueAndContratOrEmplacementContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT a.id as id, a.dateTime as dateTime, a.contratConcession as contratConcession, " +
           "a.numeroAccord as numeroAccord, a.objetConcession as objetConcession, a.concessionnaire as concessionnaire, " +
           "a.dureeAnnees as dureeAnnees, a.conditionsFinancieres as conditionsFinancieres, " +
           "a.emplacement as emplacement, a.coordonneesGps as coordonneesGps, " +
           "a.rapportTransfertGestion as rapportTransfertGestion, a.dateDebutConcession as dateDebutConcession, " +
           "a.dateFinConcession as dateFinConcession, " +
           "a.document as document, a.status as status, a.doneBy as doneBy, a.sectionCategory as sectionCategory " +
           "FROM AccordConcession a WHERE a.active = true")
    Page<AccordConcessionProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<AccordConcession> findByIdAndActiveTrue(Long id);
    Optional<AccordConcession> findByContratConcessionAndActiveTrue(String contratConcession);
    boolean existsByContratConcessionAndActiveTrue(String contratConcession);
}