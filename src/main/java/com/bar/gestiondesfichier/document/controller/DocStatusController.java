package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.DocStatus;
import com.bar.gestiondesfichier.document.projection.DocStatusProjection;
import com.bar.gestiondesfichier.document.repository.DocStatusRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * REST controller for DocStatus management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/doc-status")
@DocumentControllerCors
@Tag(name = "Document Status Management", description = "Document Status CRUD operations with pagination")
@Slf4j
public class DocStatusController {

    private final DocStatusRepository docStatusRepository;

    public DocStatusController(DocStatusRepository docStatusRepository) {
        this.docStatusRepository = docStatusRepository;
    }

    @GetMapping
    @Operation(summary = "Get all document statuses", description = "Retrieve paginated list of document statuses with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document statuses retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllDocStatuses(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        try {
            log.info("Retrieving document statuses - page: {}, size: {}, sort: {} {}", page, size, sort, direction);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<DocStatusProjection> statuses = docStatusRepository.findAllActiveProjections(pageable);
            
            return ResponseUtil.successWithPagination(statuses);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for document status retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving document statuses", e);
            return ResponseUtil.badRequest("Failed to retrieve document statuses: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document status by ID", description = "Retrieve a specific document status by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document status retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Document status not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getDocStatusById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid document status ID");
            }
            
            log.info("Retrieving document status by ID: {}", id);
            Optional<DocStatus> status = docStatusRepository.findByIdAndActiveTrue(id);
            
            if (status.isPresent()) {
                return ResponseUtil.success(status.get(), "Document status retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Document status not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving document status with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve document status: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create document status", description = "Create a new document status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Document status created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createDocStatus(@RequestBody DocStatus docStatus) {
        try {
            log.info("Creating new document status: {}", docStatus.getName());
            
            // Validate required fields
            if (docStatus.getName() == null || docStatus.getName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Document status name is required");
            }
            
            // Check if status with this name already exists
            if (docStatusRepository.existsByNameAndActiveTrue(docStatus.getName())) {
                return ResponseUtil.badRequest("Document status with this name already exists");
            }
            
            docStatus.setActive(true);
            DocStatus savedStatus = docStatusRepository.save(docStatus);
            
            return ResponseUtil.success(savedStatus, "Document status created successfully");
        } catch (Exception e) {
            log.error("Error creating document status", e);
            return ResponseUtil.badRequest("Failed to create document status: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document status", description = "Update an existing document status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Document status not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updateDocStatus(@PathVariable Long id, @RequestBody DocStatus docStatus) {
        try {
            log.info("Updating document status with ID: {}", id);
            
            Optional<DocStatus> existingStatusOpt = docStatusRepository.findByIdAndActiveTrue(id);
            if (existingStatusOpt.isEmpty()) {
                return ResponseUtil.badRequest("Document status not found with ID: " + id);
            }
            
            DocStatus existingStatus = existingStatusOpt.get();
            
            // Update fields
            if (docStatus.getName() != null) {
                existingStatus.setName(docStatus.getName());
            }
            if (docStatus.getDescription() != null) {
                existingStatus.setDescription(docStatus.getDescription());
            }
            
            DocStatus savedStatus = docStatusRepository.save(existingStatus);
            return ResponseUtil.success(savedStatus, "Document status updated successfully");
        } catch (Exception e) {
            log.error("Error updating document status with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update document status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document status", description = "Soft delete a document status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document status deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Document status not found")
    })
    public ResponseEntity<Map<String, Object>> deleteDocStatus(@PathVariable Long id) {
        try {
            log.info("Deleting document status with ID: {}", id);
            
            Optional<DocStatus> statusOpt = docStatusRepository.findByIdAndActiveTrue(id);
            if (statusOpt.isEmpty()) {
                return ResponseUtil.badRequest("Document status not found with ID: " + id);
            }
            
            DocStatus status = statusOpt.get();
            status.setActive(false);
            docStatusRepository.save(status);
            
            return ResponseUtil.success(null, "Document status deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting document status with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete document status: " + e.getMessage());
        }
    }
}