package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.AccordConcession;
import com.bar.gestiondesfichier.document.projection.AccordConcessionProjection;
import com.bar.gestiondesfichier.document.repository.AccordConcessionRepository;
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
 * REST controller for Concession Agreement management with 20-record default
 * pagination
 */
@RestController
@RequestMapping("/api/document/accord-concession")
@DocumentControllerCors
@Tag(name = "Concession Agreement Management", description = "Concession Agreement CRUD operations with pagination")
public class AccordConcessionController {

    private static final Logger log = LoggerFactory.getLogger(AccordConcessionController.class);

    private final AccordConcessionRepository accordConcessionRepository;

    public AccordConcessionController(AccordConcessionRepository accordConcessionRepository) {
        this.accordConcessionRepository = accordConcessionRepository;
    }

        @GetMapping
    @Operation(summary = "Get all concession agreements", description = "Retrieve paginated list of concession agreements with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concession agreements retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Page<AccordConcessionProjection>> getAllAccordConcession(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "numeroAccord") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Filter by status ID") @RequestParam(required = false) Long statusId,
            @Parameter(description = "Filter by document ID") @RequestParam(required = false) Long documentId,
            @Parameter(description = "Filter by section category ID") @RequestParam(required = false) Long sectionCategoryId,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        try {
            log.info("Retrieving concession agreements - page: {}, size: {}, sort: {} {}, statusId: {}, search: {}",
                    page, size, sort, direction, statusId, search);

            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<AccordConcessionProjection> accordConcessions;

            if (search != null && !search.trim().isEmpty()) {
                accordConcessions = accordConcessionRepository.findByActiveTrueAndNumeroAccordOrObjetConcessionContainingProjections(search, pageable);
            } else if (statusId != null) {
                accordConcessions = accordConcessionRepository.findByActiveTrueAndStatusIdProjections(statusId, pageable);
            } else if (sectionCategoryId != null) {
                accordConcessions = accordConcessionRepository.findByActiveTrueAndSectionCategoryIdProjections(sectionCategoryId, pageable);
            } else if (documentId != null) {
                accordConcessions = accordConcessionRepository.findByActiveTrueAndDocumentIdProjections(documentId, pageable);
            } else {
                accordConcessions = accordConcessionRepository.findAllActiveProjections(pageable);
            }

            return ResponseEntity.ok(accordConcessions);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for concession agreement retrieval: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error retrieving concession agreements", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get concession agreement by ID", description = "Retrieve a specific concession agreement record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concession agreement retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Concession agreement not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getAccordConcessionById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid concession agreement ID");
            }

            log.info("Retrieving concession agreement by ID: {}", id);
            Optional<AccordConcession> accordConcession = accordConcessionRepository.findByIdAndActiveTrue(id);

            if (accordConcession.isPresent()) {
                return ResponseUtil.success(accordConcession.get(), "Concession agreement retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Concession agreement not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving concession agreement with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve concession agreement: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create concession agreement", description = "Create a new concession agreement record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Concession agreement created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createAccordConcession(@RequestBody AccordConcession accordConcession) {
        try {
            log.info("Creating new concession agreement: {}", accordConcession.getNumeroAccord());

            // Validate required fields
            if (accordConcession.getNumeroAccord() == null || accordConcession.getNumeroAccord().trim().isEmpty()) {
                return ResponseUtil.badRequest("Numero accord is required");
            }

            if (accordConcession.getDoneBy() == null) {
                return ResponseUtil.badRequest("DoneBy (Account) is required");
            }

            if (accordConcession.getDocument() == null) {
                return ResponseUtil.badRequest("Document is required");
            }

            if (accordConcession.getStatus() == null) {
                return ResponseUtil.badRequest("Status is required");
            }

            accordConcession.setActive(true);
            AccordConcession savedAccordConcession = accordConcessionRepository.save(accordConcession);

            return ResponseUtil.success(savedAccordConcession, "Concession agreement created successfully");
        } catch (Exception e) {
            log.error("Error creating concession agreement", e);
            return ResponseUtil.badRequest("Failed to create concession agreement: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update concession agreement", description = "Update an existing concession agreement record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concession agreement updated successfully"),
        @ApiResponse(responseCode = "400", description = "Concession agreement not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updateAccordConcession(@PathVariable Long id, @RequestBody AccordConcession accordConcession) {
        try {
            log.info("Updating concession agreement with ID: {}", id);

            Optional<AccordConcession> existingAccordConcessionOpt = accordConcessionRepository.findByIdAndActiveTrue(id);
            if (existingAccordConcessionOpt.isEmpty()) {
                return ResponseUtil.badRequest("Concession agreement not found with ID: " + id);
            }

            AccordConcession existingAccordConcession = existingAccordConcessionOpt.get();

            // Update fields
            if (accordConcession.getNumeroAccord() != null) {
                existingAccordConcession.setNumeroAccord(accordConcession.getNumeroAccord());
            }
            if (accordConcession.getObjetConcession() != null) {
                existingAccordConcession.setObjetConcession(accordConcession.getObjetConcession());
            }
            if (accordConcession.getConcessionnaire() != null) {
                existingAccordConcession.setConcessionnaire(accordConcession.getConcessionnaire());
            }
            if (accordConcession.getDureeAnnees() != null) {
                existingAccordConcession.setDureeAnnees(accordConcession.getDureeAnnees());
            }
            if (accordConcession.getConditionsFinancieres() != null) {
                existingAccordConcession.setConditionsFinancieres(accordConcession.getConditionsFinancieres());
            }
            if (accordConcession.getSectionCategory() != null) {
                existingAccordConcession.setSectionCategory(accordConcession.getSectionCategory());
            }
            if (accordConcession.getStatus() != null) {
                existingAccordConcession.setStatus(accordConcession.getStatus());
            }

            AccordConcession savedAccordConcession = accordConcessionRepository.save(existingAccordConcession);
            return ResponseUtil.success(savedAccordConcession, "Concession agreement updated successfully");
        } catch (Exception e) {
            log.error("Error updating concession agreement with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update concession agreement: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete concession agreement", description = "Soft delete a concession agreement record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concession agreement deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Concession agreement not found")
    })
    public ResponseEntity<Map<String, Object>> deleteAccordConcession(@PathVariable Long id) {
        try {
            log.info("Deleting concession agreement with ID: {}", id);

            Optional<AccordConcession> accordConcessionOpt = accordConcessionRepository.findByIdAndActiveTrue(id);
            if (accordConcessionOpt.isEmpty()) {
                return ResponseUtil.badRequest("Concession agreement not found with ID: " + id);
            }

            AccordConcession accordConcession = accordConcessionOpt.get();
            accordConcession.setActive(false);
            accordConcessionRepository.save(accordConcession);

            return ResponseUtil.success(null, "Concession agreement deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting concession agreement with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete concession agreement: " + e.getMessage());
        }
    }
}
