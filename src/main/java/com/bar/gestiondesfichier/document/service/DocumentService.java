package com.bar.gestiondesfichier.document.service;

import com.bar.gestiondesfichier.document.model.Document;
import com.bar.gestiondesfichier.document.projection.DocumentProjection;
import com.bar.gestiondesfichier.document.projection.DocumentSummaryProjection;
import com.bar.gestiondesfichier.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Document domain operations
 */
public interface DocumentService {

    // Basic CRUD operations
    Page<Document> findAllActive(Pageable pageable);
    
    Optional<Document> findByIdAndActive(Long id);
    
    Document save(Document document);
    
    Document update(Document document);
    
    void deleteById(Long id);
    
    void softDeleteById(Long id);

    // Business operations
    Document createDocument(String fileName, String originalFileName, String contentType,
                           Long fileSize, String filePath, Account owner);
    
    Document updateDocument(Long id, String fileName, String originalFileName, String contentType,
                           Long fileSize, String filePath);

    // Search and filter operations
    Page<Document> findByOwner(Long ownerId, Pageable pageable);
    
    Page<Document> searchByFileName(String search, Pageable pageable);

    // Projection operations
    Page<DocumentProjection> findAllProjections(Pageable pageable);
    
    Page<DocumentSummaryProjection> findAllSummaries(Pageable pageable);

    // Statistics
    long countActiveDocuments();
    
    long countActiveDocumentsByOwner(Long ownerId);
    
    Long getTotalFileSize();
    
    Long getTotalFileSizeByOwner(Long ownerId);

    // Validation
    boolean existsByFileName(String fileName);
}