package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.EquipmentId;
import com.bar.gestiondesfichier.document.projection.EquipmentIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for EquipmentId with pagination and projection support
 */
@Repository
public interface EquipmentIdRepository extends JpaRepository<EquipmentId, Long> {

    Page<EquipmentId> findByActiveTrue(Pageable pageable);
    Page<EquipmentId> findByActiveTrueAndStatus_Id(Long statusId, Pageable pageable);
    Page<EquipmentId> findByActiveTrueAndDocument_Id(Long documentId, Pageable pageable);
    Page<EquipmentId> findByActiveTrueAndEquipmentType(String equipmentType, Pageable pageable);
    
    @Query("SELECT e FROM EquipmentId e WHERE e.active = true AND " +
           "(LOWER(e.equipmentName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.location) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EquipmentId> findByActiveTrueAndEquipmentNameOrLocationContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT e FROM EquipmentId e WHERE e.active = true AND " +
           "LOWER(e.equipmentType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.serialNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.plateNumber) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<EquipmentId> findByActiveTrueAndSearchTerms(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT e.id as id, e.dateTime as dateTime, e.equipmentType as equipmentType, " +
           "e.serialNumber as serialNumber, e.plateNumber as plateNumber, " +
           "e.etatEquipement as etatEquipement, e.dateAchat as dateAchat, " +
           "e.dateVisiteTechnique as dateVisiteTechnique, e.assurance as assurance, " +
           "e.documentsTelecharger as documentsTelecharger, " +
           "e.document as document, e.status as status, e.doneBy as doneBy " +
           "FROM EquipmentId e WHERE e.active = true")
    Page<EquipmentIdProjection> findAllActiveProjections(Pageable pageable);
    
    Optional<EquipmentId> findByIdAndActiveTrue(Long id);
    Optional<EquipmentId> findBySerialNumberAndActiveTrue(String serialNumber);
    Optional<EquipmentId> findByPlateNumberAndActiveTrue(String plateNumber);
    boolean existsBySerialNumberAndActiveTrue(String serialNumber);
    boolean existsByPlateNumberAndActiveTrue(String plateNumber);
}