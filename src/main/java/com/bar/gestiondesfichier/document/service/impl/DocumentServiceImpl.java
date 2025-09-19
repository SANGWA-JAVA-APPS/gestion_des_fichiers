package com.bar.gestiondesfichier.document.service.impl;

import com.bar.gestiondesfichier.document.model.Document;
import com.bar.gestiondesfichier.document.projection.DocumentProjection;
import com.bar.gestiondesfichier.document.projection.DocumentSummaryProjection;
import com.bar.gestiondesfichier.document.repository.DocumentRepository;
import com.bar.gestiondesfichier.document.service.DocumentService;
import com.bar.gestiondesfichier.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Document service implementation with comprehensive error handling and logging
 */
@Service
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Page<Document> findAllActive(Pageable pageable) {
        try {
            log.debug("Finding all active documents with pagination: {}", pageable);
            return documentRepository.findByActiveTrue(pageable);
        } catch (Exception e) {
            log.error("Error finding active documents", e);
            throw new RuntimeException("Failed to retrieve documents: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Document> findByIdAndActive(Long id) {
        try {
            log.debug("Finding active document by id: {}", id);
            return documentRepository.findByIdAndActiveTrue(id);
        } catch (Exception e) {
            log.error("Error finding document by id: {}", id, e);
            throw new RuntimeException("Failed to retrieve document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Document save(Document document) {
        try {
            log.debug("Saving document: {}", document.getFileName());
            return documentRepository.save(document);
        } catch (Exception e) {
            log.error("Error saving document: {}", document.getFileName(), e);
            throw new RuntimeException("Failed to save document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Document update(Document document) {
        try {
            log.debug("Updating document with id: {}", document.getId());
            return documentRepository.save(document);
        } catch (Exception e) {
            log.error("Error updating document with id: {}", document.getId(), e);
            throw new RuntimeException("Failed to update document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            log.debug("Deleting document with id: {}", id);
            documentRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting document with id: {}", id, e);
            throw new RuntimeException("Failed to delete document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        try {
            log.debug("Soft deleting document with id: {}", id);
            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));
            document.setActive(false);
            documentRepository.save(document);
        } catch (Exception e) {
            log.error("Error soft deleting document with id: {}", id, e);
            throw new RuntimeException("Failed to delete document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Document createDocument(String fileName, String originalFileName, String contentType,
                                 Long fileSize, String filePath, Account owner) {
        try {
            log.debug("Creating new document: {} for owner: {}", fileName, owner.getUsername());
            
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new IllegalArgumentException("File name is required");
            }
            
            if (filePath == null || filePath.trim().isEmpty()) {
                throw new IllegalArgumentException("File path is required");
            }
            
            if (owner == null) {
                throw new IllegalArgumentException("Document owner is required");
            }
            
            if (existsByFileName(fileName)) {
                throw new IllegalArgumentException("Document with name '" + fileName + "' already exists");
            }
            
            Document document = new Document(fileName.trim(), originalFileName, contentType,
                                           fileSize, filePath.trim(), owner);
            
            return documentRepository.save(document);
        } catch (Exception e) {
            log.error("Error creating document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Document updateDocument(Long id, String fileName, String originalFileName, String contentType,
                                 Long fileSize, String filePath) {
        try {
            log.debug("Updating document with id: {}", id);
            
            Document document = documentRepository.findByIdAndActiveTrue(id)
                    .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));
            
            if (fileName != null && !fileName.trim().isEmpty()) {
                if (!document.getFileName().equals(fileName) && existsByFileName(fileName)) {
                    throw new IllegalArgumentException("Document with name '" + fileName + "' already exists");
                }
                document.setFileName(fileName.trim());
            }
            
            if (originalFileName != null) {
                document.setOriginalFileName(originalFileName);
            }
            
            if (contentType != null) {
                document.setContentType(contentType);
            }
            
            if (fileSize != null) {
                document.setFileSize(fileSize);
            }
            
            if (filePath != null && !filePath.trim().isEmpty()) {
                document.setFilePath(filePath.trim());
            }
            
            return documentRepository.save(document);
        } catch (Exception e) {
            log.error("Error updating document with id: {}", id, e);
            throw new RuntimeException("Failed to update document: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Document> findByOwner(Long ownerId, Pageable pageable) {
        try {
            log.debug("Finding documents by owner: {} with pagination: {}", ownerId, pageable);
            return documentRepository.findByActiveTrueAndOwner_Id(ownerId, pageable);
        } catch (Exception e) {
            log.error("Error finding documents by owner: {}", ownerId, e);
            throw new RuntimeException("Failed to retrieve documents by owner: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Document> searchByFileName(String search, Pageable pageable) {
        try {
            log.debug("Searching documents by filename: {} with pagination: {}", search, pageable);
            return documentRepository.findByActiveTrueAndFileNameContaining(search, pageable);
        } catch (Exception e) {
            log.error("Error searching documents by filename: {}", search, e);
            throw new RuntimeException("Failed to search documents: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<DocumentProjection> findAllProjections(Pageable pageable) {
        try {
            log.debug("Finding all active document projections with pagination: {}", pageable);
            return documentRepository.findAllActiveProjections(pageable);
        } catch (Exception e) {
            log.error("Error finding document projections", e);
            throw new RuntimeException("Failed to retrieve document projections: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<DocumentSummaryProjection> findAllSummaries(Pageable pageable) {
        try {
            log.debug("Finding all active document summaries with pagination: {}", pageable);
            return documentRepository.findAllSummaryProjections(pageable);
        } catch (Exception e) {
            log.error("Error finding document summaries", e);
            throw new RuntimeException("Failed to retrieve document summaries: " + e.getMessage(), e);
        }
    }

    @Override
    public long countActiveDocuments() {
        try {
            return documentRepository.countActiveDocuments();
        } catch (Exception e) {
            log.error("Error counting active documents", e);
            return 0;
        }
    }

    @Override
    public long countActiveDocumentsByOwner(Long ownerId) {
        try {
            return documentRepository.countActiveDocumentsByOwner(ownerId);
        } catch (Exception e) {
            log.error("Error counting active documents by owner: {}", ownerId, e);
            return 0;
        }
    }

    @Override
    public Long getTotalFileSize() {
        try {
            return documentRepository.getTotalFileSize();
        } catch (Exception e) {
            log.error("Error getting total file size", e);
            return 0L;
        }
    }

    @Override
    public Long getTotalFileSizeByOwner(Long ownerId) {
        try {
            return documentRepository.getTotalFileSizeByOwner(ownerId);
        } catch (Exception e) {
            log.error("Error getting total file size by owner: {}", ownerId, e);
            return 0L;
        }
    }

    @Override
    public boolean existsByFileName(String fileName) {
        try {
            return documentRepository.existsByFileNameAndActiveTrue(fileName);
        } catch (Exception e) {
            log.error("Error checking if document exists by filename: {}", fileName, e);
            return false;
        }
    }
}