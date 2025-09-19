package com.bar.gestiondesfichier.document.repository;

import com.bar.gestiondesfichier.document.model.Document;
import com.bar.gestiondesfichier.document.projection.DocumentProjection;
import com.bar.gestiondesfichier.document.projection.DocumentSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Document domain with pagination and projection support
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Pagination methods
    Page<Document> findByActiveTrue(Pageable pageable);
    
    Page<Document> findByActiveTrueAndOwner_Id(Long ownerId, Pageable pageable);
    
    @Query("SELECT d FROM Document d WHERE d.active = true AND " +
           "LOWER(d.fileName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.originalFileName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Document> findByActiveTrueAndFileNameContaining(@Param("search") String search, Pageable pageable);

    // Projection methods
    @Query("SELECT d.id as id, d.fileName as fileName, d.originalFileName as originalFileName, " +
           "d.contentType as contentType, d.fileSize as fileSize, d.createdAt as createdAt, " +
           "d.updatedAt as updatedAt, d.active as active, " +
           "d.owner as owner " +
           "FROM Document d WHERE d.active = true")
    Page<DocumentProjection> findAllActiveProjections(Pageable pageable);

    @Query("SELECT d.id as id, d.fileName as fileName, d.contentType as contentType, " +
           "d.fileSize as fileSize, CONCAT(d.owner.fullName) as ownerName, " +
           "CAST(d.createdAt as string) as createdAt " +
           "FROM Document d WHERE d.active = true")
    Page<DocumentSummaryProjection> findAllSummaryProjections(Pageable pageable);

    // Specific queries
    Optional<Document> findByIdAndActiveTrue(Long id);
    
    List<Document> findByActiveTrueAndOwner_Id(Long ownerId);
    
    @Query("SELECT COUNT(d) FROM Document d WHERE d.active = true")
    long countActiveDocuments();
    
    @Query("SELECT COUNT(d) FROM Document d WHERE d.active = true AND d.owner.id = :ownerId")
    long countActiveDocumentsByOwner(@Param("ownerId") Long ownerId);
    
    boolean existsByFileNameAndActiveTrue(String fileName);
    
    @Query("SELECT SUM(d.fileSize) FROM Document d WHERE d.active = true")
    Long getTotalFileSize();
    
    @Query("SELECT SUM(d.fileSize) FROM Document d WHERE d.active = true AND d.owner.id = :ownerId")
    Long getTotalFileSizeByOwner(@Param("ownerId") Long ownerId);
}