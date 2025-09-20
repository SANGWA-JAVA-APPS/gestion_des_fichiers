package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.Estate;
import com.bar.gestiondesfichier.document.projection.EstateProjection;
import com.bar.gestiondesfichier.document.repository.EstateRepository;
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
 * REST controller for Estate management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/estate")
@DocumentControllerCors
@Tag(name = "Estate Management", description = "Estate CRUD operations with pagination")
@Slf4j
public class EstateController {

    private final EstateRepository estateRepository;

    public EstateController(EstateRepository estateRepository) {
        this.estateRepository = estateRepository;
    }

    @GetMapping
    @Operation(summary = "Get all estates", description = "Retrieve paginated list of estates with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estates retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllEstate(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "propertyName") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Filter by status ID") @RequestParam(required = false) Long statusId,
            @Parameter(description = "Filter by document ID") @RequestParam(required = false) Long documentId,
            @Parameter(description = "Filter by section category ID") @RequestParam(required = false) Long sectionCategoryId,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        try {
            log.info("Retrieving estates - page: {}, size: {}, sort: {} {}, statusId: {}, search: {}", 
                    page, size, sort, direction, statusId, search);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<EstateProjection> estates;
            
            if (search != null && !search.trim().isEmpty()) {
                estates = estateRepository.findByActiveTrueAndPropertyNameOrLocationContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                estates = estateRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else if (sectionCategoryId != null) {
                estates = estateRepository.findByActiveTrueAndSectionCategory_Id(sectionCategoryId, pageable)
                    .map(this::convertToProjection);
            } else if (documentId != null) {
                estates = estateRepository.findByActiveTrueAndDocument_Id(documentId, pageable)
                    .map(this::convertToProjection);
            } else {
                estates = estateRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(estates);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for estate retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving estates", e);
            return ResponseUtil.badRequest("Failed to retrieve estates: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get estate by ID", description = "Retrieve a specific estate record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estate retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Estate not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getEstateById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid estate ID");
            }
            
            log.info("Retrieving estate by ID: {}", id);
            Optional<Estate> estate = estateRepository.findByIdAndActiveTrue(id);
            
            if (estate.isPresent()) {
                return ResponseUtil.success(estate.get(), "Estate retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Estate not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving estate with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve estate: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create estate", description = "Create a new estate record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estate created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createEstate(@RequestBody Estate estate) {
        try {
            log.info("Creating new estate: {}", estate.getPropertyName());
            
            // Validate required fields
            if (estate.getPropertyName() == null || estate.getPropertyName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Property name is required");
            }
            
            if (estate.getDoneBy() == null) {
                return ResponseUtil.badRequest("DoneBy (Account) is required");
            }
            
            if (estate.getDocument() == null) {
                return ResponseUtil.badRequest("Document is required");
            }
            
            if (estate.getStatus() == null) {
                return ResponseUtil.badRequest("Status is required");
            }
            
            estate.setActive(true);
            Estate savedEstate = estateRepository.save(estate);
            
            return ResponseUtil.success(savedEstate, "Estate created successfully");
        } catch (Exception e) {
            log.error("Error creating estate", e);
            return ResponseUtil.badRequest("Failed to create estate: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update estate", description = "Update an existing estate record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estate updated successfully"),
        @ApiResponse(responseCode = "400", description = "Estate not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updateEstate(@PathVariable Long id, @RequestBody Estate estate) {
        try {
            log.info("Updating estate with ID: {}", id);
            
            Optional<Estate> existingEstateOpt = estateRepository.findByIdAndActiveTrue(id);
            if (existingEstateOpt.isEmpty()) {
                return ResponseUtil.badRequest("Estate not found with ID: " + id);
            }
            
            Estate existingEstate = existingEstateOpt.get();
            
            // Update fields
            if (estate.getPropertyName() != null) {
                existingEstate.setPropertyName(estate.getPropertyName());
            }
            if (estate.getLocation() != null) {
                existingEstate.setLocation(estate.getLocation());
            }
            if (estate.getPropertyType() != null) {
                existingEstate.setPropertyType(estate.getPropertyType());
            }
            if (estate.getEstateValue() != null) {
                existingEstate.setEstateValue(estate.getEstateValue());
            }
            if (estate.getOwnershipStatus() != null) {
                existingEstate.setOwnershipStatus(estate.getOwnershipStatus());
            }
            if (estate.getSectionCategory() != null) {
                existingEstate.setSectionCategory(estate.getSectionCategory());
            }
            if (estate.getStatus() != null) {
                existingEstate.setStatus(estate.getStatus());
            }
            
            Estate savedEstate = estateRepository.save(existingEstate);
            return ResponseUtil.success(savedEstate, "Estate updated successfully");
        } catch (Exception e) {
            log.error("Error updating estate with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update estate: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete estate", description = "Soft delete an estate record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estate deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Estate not found")
    })
    public ResponseEntity<Map<String, Object>> deleteEstate(@PathVariable Long id) {
        try {
            log.info("Deleting estate with ID: {}", id);
            
            Optional<Estate> estateOpt = estateRepository.findByIdAndActiveTrue(id);
            if (estateOpt.isEmpty()) {
                return ResponseUtil.badRequest("Estate not found with ID: " + id);
            }
            
            Estate estate = estateOpt.get();
            estate.setActive(false);
            estateRepository.save(estate);
            
            return ResponseUtil.success(null, "Estate deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting estate with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete estate: " + e.getMessage());
        }
    }

    // Helper method to convert entity to projection
    private EstateProjection convertToProjection(Estate estate) {
        return new EstateProjection() {
            @Override
            public Long getId() { return estate.getId(); }
            
            @Override
            public java.time.LocalDateTime getDateTime() { return estate.getDateTime(); }
            
            @Override
            public String getReference() { return estate.getReference(); }
            
            @Override
            public String getEstateType() { return estate.getEstateType(); }
            
            @Override
            public String getEmplacement() { return estate.getEmplacement(); }
            
            @Override
            public String getCoordonneesGps() { return estate.getCoordonneesGps(); }
            
            @Override
            public java.time.LocalDateTime getDateOfBuilding() { return estate.getDateOfBuilding(); }
            
            @Override
            public String getComments() { return estate.getComments(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return estate.getDocument().getId(); }
                    
                    @Override
                    public String getFileName() { return estate.getDocument().getFileName(); }
                    
                    @Override
                    public String getOriginalFileName() { return estate.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return estate.getStatus().getId(); }
                    
                    @Override
                    public String getName() { return estate.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return estate.getDoneBy().getId(); }
                    
                    @Override
                    public String getFullName() { return estate.getDoneBy().getFullName(); }
                    
                    @Override
                    public String getUsername() { return estate.getDoneBy().getUsername(); }
                };
            }
            
            @Override
            public SectionCategoryInfo getSectionCategory() {
                if (estate.getSectionCategory() == null) return null;
                return new SectionCategoryInfo() {
                    @Override
                    public Long getId() { return estate.getSectionCategory().getId(); }
                    
                    @Override
                    public String getName() { return estate.getSectionCategory().getName(); }
                };
            }
        };
    }
}