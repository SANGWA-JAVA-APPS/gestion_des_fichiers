package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.Document;
import com.bar.gestiondesfichier.document.projection.DocumentProjection;
import com.bar.gestiondesfichier.document.projection.DocumentSummaryProjection;
import com.bar.gestiondesfichier.document.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Document management controller with standardized responses and comprehensive error handling
 */
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Document Management", description = "Document CRUD operations with pagination and projections")
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    @Operation(summary = "Get all documents", 
               description = "Retrieve paginated list of active documents with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documents retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllDocuments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "fileName") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        try {
            log.info("Retrieving documents - page: {}, size: {}, sort: {} {}", page, size, sort, direction);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Document> documents = documentService.findAllActive(pageable);
            
            return ResponseUtil.successWithPagination(documents);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for document retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving documents", e);
            return ResponseUtil.badRequest("Failed to retrieve documents: " + e.getMessage());
        }
    }

    @GetMapping("/projections")
    @Operation(summary = "Get document projections", 
               description = "Retrieve paginated list of document projections with optimized data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document projections retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<Map<String, Object>> getAllDocumentProjections(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "fileName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            log.info("Retrieving document projections - page: {}, size: {}", page, size);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<DocumentProjection> projections = documentService.findAllProjections(pageable);
            
            return ResponseUtil.successWithPagination(projections);
        } catch (Exception e) {
            log.error("Error retrieving document projections", e);
            return ResponseUtil.badRequest("Failed to retrieve document projections: " + e.getMessage());
        }
    }

    @GetMapping("/summaries")
    @Operation(summary = "Get document summaries", 
               description = "Retrieve paginated list of document summaries for dashboard")
    public ResponseEntity<Map<String, Object>> getAllDocumentSummaries(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            log.info("Retrieving document summaries - page: {}, size: {}", page, size);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, "createdAt", "desc");
            Page<DocumentSummaryProjection> summaries = documentService.findAllSummaries(pageable);
            
            return ResponseUtil.successWithPagination(summaries);
        } catch (Exception e) {
            log.error("Error retrieving document summaries", e);
            return ResponseUtil.badRequest("Failed to retrieve document summaries: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID", description = "Retrieve a specific document by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Document not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getDocumentById(
            @Parameter(description = "Document ID") @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid document ID");
            }
            
            log.info("Retrieving document by ID: {}", id);
            Optional<Document> document = documentService.findByIdAndActive(id);
            
            if (document.isPresent()) {
                return ResponseUtil.success(document.get(), "Document retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Document not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving document with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve document: " + e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get documents by owner", description = "Retrieve documents owned by specific user")
    public ResponseEntity<Map<String, Object>> getDocumentsByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "fileName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            if (ownerId == null || ownerId <= 0) {
                return ResponseUtil.badRequest("Invalid owner ID");
            }
            
            log.info("Retrieving documents by owner: {}", ownerId);
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Document> documents = documentService.findByOwner(ownerId, pageable);
            
            return ResponseUtil.successWithPagination(documents);
        } catch (Exception e) {
            log.error("Error retrieving documents by owner: {}", ownerId, e);
            return ResponseUtil.badRequest("Failed to retrieve documents: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search documents", description = "Search documents by filename")
    public ResponseEntity<Map<String, Object>> searchDocuments(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "fileName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return ResponseUtil.badRequest("Search query is required");
            }
            
            log.info("Searching documents with query: {}", query);
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Document> documents = documentService.searchByFileName(query.trim(), pageable);
            
            return ResponseUtil.successWithPagination(documents);
        } catch (Exception e) {
            log.error("Error searching documents with query: {}", query, e);
            return ResponseUtil.badRequest("Failed to search documents: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create document", description = "Create a new document")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> createDocument(@RequestBody Document documentRequest) {
        try {
            log.info("Creating document: {}", documentRequest.getFileName());
            
            Document document = documentService.createDocument(
                    documentRequest.getFileName(),
                    documentRequest.getOriginalFileName(),
                    documentRequest.getContentType(),
                    documentRequest.getFileSize(),
                    documentRequest.getFilePath(),
                    documentRequest.getOwner()
            );
            
            return ResponseUtil.success(document, "Document created successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Invalid document data: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error creating document", e);
            return ResponseUtil.badRequest("Failed to create document: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document", description = "Update an existing document")
    public ResponseEntity<Map<String, Object>> updateDocument(
            @PathVariable Long id, @RequestBody Document documentRequest) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid document ID");
            }
            
            log.info("Updating document with ID: {}", id);
            
            Document document = documentService.updateDocument(
                    id,
                    documentRequest.getFileName(),
                    documentRequest.getOriginalFileName(),
                    documentRequest.getContentType(),
                    documentRequest.getFileSize(),
                    documentRequest.getFilePath()
            );
            
            return ResponseUtil.success(document, "Document updated successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Invalid document data for update: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating document with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update document: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document", description = "Soft delete a document")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid document ID");
            }
            
            log.info("Deleting document with ID: {}", id);
            documentService.softDeleteById(id);
            
            return ResponseUtil.success(null, "Document deleted successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Document not found for deletion: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting document with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete document: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Get document statistics", description = "Retrieve document statistics")
    public ResponseEntity<Map<String, Object>> getDocumentStats() {
        try {
            log.info("Retrieving document statistics");
            
            Map<String, Object> stats = Map.of(
                "totalDocuments", documentService.countActiveDocuments(),
                "totalFileSize", documentService.getTotalFileSize()
            );
            
            return ResponseUtil.success(stats, "Document statistics retrieved successfully");
        } catch (Exception e) {
            log.error("Error retrieving document statistics", e);
            return ResponseUtil.badRequest("Failed to retrieve statistics: " + e.getMessage());
        }
    }
}