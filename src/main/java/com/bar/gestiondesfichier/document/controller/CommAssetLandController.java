package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.CommAssetLand;
import com.bar.gestiondesfichier.document.projection.CommAssetLandProjection;
import com.bar.gestiondesfichier.document.repository.CommAssetLandRepository;
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
 * REST controller for Commercial Asset Land management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/comm-asset-land")
@DocumentControllerCors
@Tag(name = "Commercial Asset Land Management", description = "Commercial Asset Land CRUD operations with pagination")
@Slf4j
public class CommAssetLandController {

    private final CommAssetLandRepository commAssetLandRepository;

    public CommAssetLandController(CommAssetLandRepository commAssetLandRepository) {
        this.commAssetLandRepository = commAssetLandRepository;
    }

    @GetMapping
    @Operation(summary = "Get all commercial asset land records", description = "Retrieve paginated list of commercial asset land with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Commercial asset land records retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllCommAssetLand(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "assetName") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Filter by status ID") @RequestParam(required = false) Long statusId,
            @Parameter(description = "Filter by document ID") @RequestParam(required = false) Long documentId,
            @Parameter(description = "Filter by section category ID") @RequestParam(required = false) Long sectionCategoryId,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        try {
            log.info("Retrieving commercial asset land records - page: {}, size: {}, sort: {} {}, statusId: {}, search: {}", 
                    page, size, sort, direction, statusId, search);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<CommAssetLandProjection> commAssetLands;
            
            if (search != null && !search.trim().isEmpty()) {
                commAssetLands = commAssetLandRepository.findByActiveTrueAndAssetNameOrLocationContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                commAssetLands = commAssetLandRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else if (sectionCategoryId != null) {
                commAssetLands = commAssetLandRepository.findByActiveTrueAndSectionCategory_Id(sectionCategoryId, pageable)
                    .map(this::convertToProjection);
            } else if (documentId != null) {
                commAssetLands = commAssetLandRepository.findByActiveTrueAndDocument_Id(documentId, pageable)
                    .map(this::convertToProjection);
            } else {
                commAssetLands = commAssetLandRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(commAssetLands);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for commercial asset land retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving commercial asset land records", e);
            return ResponseUtil.badRequest("Failed to retrieve commercial asset land records: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get commercial asset land by ID", description = "Retrieve a specific commercial asset land record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Commercial asset land retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Commercial asset land not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getCommAssetLandById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid commercial asset land ID");
            }
            
            log.info("Retrieving commercial asset land by ID: {}", id);
            Optional<CommAssetLand> commAssetLand = commAssetLandRepository.findByIdAndActiveTrue(id);
            
            if (commAssetLand.isPresent()) {
                return ResponseUtil.success(commAssetLand.get(), "Commercial asset land retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Commercial asset land not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving commercial asset land with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve commercial asset land: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create commercial asset land", description = "Create a new commercial asset land record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Commercial asset land created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createCommAssetLand(@RequestBody CommAssetLand commAssetLand) {
        try {
            log.info("Creating new commercial asset land: {}", commAssetLand.getAssetName());
            
            // Validate required fields
            if (commAssetLand.getAssetName() == null || commAssetLand.getAssetName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Asset name is required");
            }
            
            if (commAssetLand.getDoneBy() == null) {
                return ResponseUtil.badRequest("DoneBy (Account) is required");
            }
            
            if (commAssetLand.getDocument() == null) {
                return ResponseUtil.badRequest("Document is required");
            }
            
            if (commAssetLand.getStatus() == null) {
                return ResponseUtil.badRequest("Status is required");
            }
            
            commAssetLand.setActive(true);
            CommAssetLand savedCommAssetLand = commAssetLandRepository.save(commAssetLand);
            
            return ResponseUtil.success(savedCommAssetLand, "Commercial asset land created successfully");
        } catch (Exception e) {
            log.error("Error creating commercial asset land", e);
            return ResponseUtil.badRequest("Failed to create commercial asset land: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update commercial asset land", description = "Update an existing commercial asset land record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Commercial asset land updated successfully"),
        @ApiResponse(responseCode = "400", description = "Commercial asset land not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updateCommAssetLand(@PathVariable Long id, @RequestBody CommAssetLand commAssetLand) {
        try {
            log.info("Updating commercial asset land with ID: {}", id);
            
            Optional<CommAssetLand> existingCommAssetLandOpt = commAssetLandRepository.findByIdAndActiveTrue(id);
            if (existingCommAssetLandOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial asset land not found with ID: " + id);
            }
            
            CommAssetLand existingCommAssetLand = existingCommAssetLandOpt.get();
            
            // Update fields
            if (commAssetLand.getAssetName() != null) {
                existingCommAssetLand.setAssetName(commAssetLand.getAssetName());
            }
            if (commAssetLand.getLocation() != null) {
                existingCommAssetLand.setLocation(commAssetLand.getLocation());
            }
            if (commAssetLand.getSurfaceArea() != null) {
                existingCommAssetLand.setSurfaceArea(commAssetLand.getSurfaceArea());
            }
            if (commAssetLand.getAssetValue() != null) {
                existingCommAssetLand.setAssetValue(commAssetLand.getAssetValue());
            }
            if (commAssetLand.getCurrentStatus() != null) {
                existingCommAssetLand.setCurrentStatus(commAssetLand.getCurrentStatus());
            }
            if (commAssetLand.getSectionCategory() != null) {
                existingCommAssetLand.setSectionCategory(commAssetLand.getSectionCategory());
            }
            if (commAssetLand.getStatus() != null) {
                existingCommAssetLand.setStatus(commAssetLand.getStatus());
            }
            
            CommAssetLand savedCommAssetLand = commAssetLandRepository.save(existingCommAssetLand);
            return ResponseUtil.success(savedCommAssetLand, "Commercial asset land updated successfully");
        } catch (Exception e) {
            log.error("Error updating commercial asset land with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update commercial asset land: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete commercial asset land", description = "Soft delete a commercial asset land record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Commercial asset land deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Commercial asset land not found")
    })
    public ResponseEntity<Map<String, Object>> deleteCommAssetLand(@PathVariable Long id) {
        try {
            log.info("Deleting commercial asset land with ID: {}", id);
            
            Optional<CommAssetLand> commAssetLandOpt = commAssetLandRepository.findByIdAndActiveTrue(id);
            if (commAssetLandOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial asset land not found with ID: " + id);
            }
            
            CommAssetLand commAssetLand = commAssetLandOpt.get();
            commAssetLand.setActive(false);
            commAssetLandRepository.save(commAssetLand);
            
            return ResponseUtil.success(null, "Commercial asset land deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting commercial asset land with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete commercial asset land: " + e.getMessage());
        }
    }

    @GetMapping("/by-section/{sectionCategoryId}")
    @Operation(summary = "Get commercial asset lands by section category", description = "Retrieve commercial asset lands filtered by section category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Commercial asset lands retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid section category ID")
    })
    public ResponseEntity<Map<String, Object>> getCommAssetLandsBySectionCategory(
            @PathVariable Long sectionCategoryId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size) {
        try {
            log.info("Retrieving commercial asset lands by section category ID: {}", sectionCategoryId);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, "assetName", "asc");
            Page<CommAssetLandProjection> commAssetLands = 
                commAssetLandRepository.findAllBySectionCategoryProjections(sectionCategoryId, pageable);
            
            return ResponseUtil.successWithPagination(commAssetLands);
        } catch (Exception e) {
            log.error("Error retrieving commercial asset lands by section category: {}", sectionCategoryId, e);
            return ResponseUtil.badRequest("Failed to retrieve commercial asset lands by section category: " + e.getMessage());
        }
    }

    // Helper method to convert entity to projection
    private CommAssetLandProjection convertToProjection(CommAssetLand commAssetLand) {
        return new CommAssetLandProjection() {
            @Override
            public Long getId() { return commAssetLand.getId(); }
            
            @Override
            public java.time.LocalDateTime getDateTime() { return commAssetLand.getDateTime(); }
            
            @Override
            public String getDescription() { return commAssetLand.getDescription(); }
            
            @Override
            public String getReference() { return commAssetLand.getReference(); }
            
            @Override
            public java.time.LocalDateTime getDateObtention() { return commAssetLand.getDateObtention(); }
            
            @Override
            public String getCoordonneesGps() { return commAssetLand.getCoordonneesGps(); }
            
            @Override
            public String getEmplacement() { return commAssetLand.getEmplacement(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return commAssetLand.getDocument().getId(); }
                    
                    @Override
                    public String getFileName() { return commAssetLand.getDocument().getFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return commAssetLand.getStatus().getId(); }
                    
                    @Override
                    public String getName() { return commAssetLand.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return commAssetLand.getDoneBy().getId(); }
                    
                    @Override
                    public String getFullName() { return commAssetLand.getDoneBy().getFullName(); }
                };
            }
            
            @Override
            public SectionInfo getSection() {
                if (commAssetLand.getSection() == null) return null;
                return new SectionInfo() {
                    @Override
                    public Long getId() { return commAssetLand.getSection().getId(); }
                    
                    @Override
                    public String getName() { return commAssetLand.getSection().getName(); }
                };
            }
        };
    }
}