package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.PermiConstruction;
import com.bar.gestiondesfichier.document.projection.PermiConstructionProjection;
import com.bar.gestiondesfichier.document.repository.PermiConstructionRepository;
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
 * REST controller for Construction Permit management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/permi-construction")
@DocumentControllerCors
@Tag(name = "Construction Permit Management", description = "Construction Permit CRUD operations with pagination")
@Slf4j
public class PermiConstructionController {

    private final PermiConstructionRepository permiConstructionRepository;

    public PermiConstructionController(PermiConstructionRepository permiConstructionRepository) {
        this.permiConstructionRepository = permiConstructionRepository;
    }

    @GetMapping
    @Operation(summary = "Get all construction permits", description = "Retrieve paginated list of construction permits with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construction permits retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllPermiConstruction(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "numeroPermis") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Filter by status ID") @RequestParam(required = false) Long statusId,
            @Parameter(description = "Filter by document ID") @RequestParam(required = false) Long documentId,
            @Parameter(description = "Filter by section category ID") @RequestParam(required = false) Long sectionCategoryId,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        try {
            log.info("Retrieving construction permits - page: {}, size: {}, sort: {} {}, statusId: {}, search: {}", 
                    page, size, sort, direction, statusId, search);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<PermiConstructionProjection> permiConstructions;
            
            if (search != null && !search.trim().isEmpty()) {
                permiConstructions = permiConstructionRepository.findByActiveTrueAndNumeroPermisOrProjetContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                permiConstructions = permiConstructionRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else if (sectionCategoryId != null) {
                permiConstructions = permiConstructionRepository.findByActiveTrueAndSectionCategory_Id(sectionCategoryId, pageable)
                    .map(this::convertToProjection);
            } else if (documentId != null) {
                permiConstructions = permiConstructionRepository.findByActiveTrueAndDocument_Id(documentId, pageable)
                    .map(this::convertToProjection);
            } else {
                permiConstructions = permiConstructionRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(permiConstructions);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for construction permit retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving construction permits", e);
            return ResponseUtil.badRequest("Failed to retrieve construction permits: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get construction permit by ID", description = "Retrieve a specific construction permit record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construction permit retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Construction permit not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getPermiConstructionById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid construction permit ID");
            }
            
            log.info("Retrieving construction permit by ID: {}", id);
            Optional<PermiConstruction> permiConstruction = permiConstructionRepository.findByIdAndActiveTrue(id);
            
            if (permiConstruction.isPresent()) {
                return ResponseUtil.success(permiConstruction.get(), "Construction permit retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Construction permit not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving construction permit with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve construction permit: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create construction permit", description = "Create a new construction permit record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Construction permit created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createPermiConstruction(@RequestBody PermiConstruction permiConstruction) {
        try {
            log.info("Creating new construction permit: {}", permiConstruction.getNumeroPermis());
            
            // Validate required fields
            if (permiConstruction.getNumeroPermis() == null || permiConstruction.getNumeroPermis().trim().isEmpty()) {
                return ResponseUtil.badRequest("Numero permis is required");
            }
            
            if (permiConstruction.getDoneBy() == null) {
                return ResponseUtil.badRequest("DoneBy (Account) is required");
            }
            
            if (permiConstruction.getDocument() == null) {
                return ResponseUtil.badRequest("Document is required");
            }
            
            if (permiConstruction.getStatus() == null) {
                return ResponseUtil.badRequest("Status is required");
            }
            
            // Check if permit number already exists
            if (permiConstructionRepository.existsByNumeroPermisAndActiveTrue(permiConstruction.getNumeroPermis())) {
                return ResponseUtil.badRequest("Construction permit with this number already exists");
            }
            
            permiConstruction.setActive(true);
            PermiConstruction savedPermiConstruction = permiConstructionRepository.save(permiConstruction);
            
            return ResponseUtil.success(savedPermiConstruction, "Construction permit created successfully");
        } catch (Exception e) {
            log.error("Error creating construction permit", e);
            return ResponseUtil.badRequest("Failed to create construction permit: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update construction permit", description = "Update an existing construction permit record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construction permit updated successfully"),
        @ApiResponse(responseCode = "400", description = "Construction permit not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updatePermiConstruction(@PathVariable Long id, @RequestBody PermiConstruction permiConstruction) {
        try {
            log.info("Updating construction permit with ID: {}", id);
            
            Optional<PermiConstruction> existingPermiConstructionOpt = permiConstructionRepository.findByIdAndActiveTrue(id);
            if (existingPermiConstructionOpt.isEmpty()) {
                return ResponseUtil.badRequest("Construction permit not found with ID: " + id);
            }
            
            PermiConstruction existingPermiConstruction = existingPermiConstructionOpt.get();
            
            // Update fields
            if (permiConstruction.getNumeroPermis() != null) {
                existingPermiConstruction.setNumeroPermis(permiConstruction.getNumeroPermis());
            }
            if (permiConstruction.getProjet() != null) {
                existingPermiConstruction.setProjet(permiConstruction.getProjet());
            }
            if (permiConstruction.getLocalisation() != null) {
                existingPermiConstruction.setLocalisation(permiConstruction.getLocalisation());
            }
            if (permiConstruction.getDateDelivrance() != null) {
                existingPermiConstruction.setDateDelivrance(permiConstruction.getDateDelivrance());
            }
            if (permiConstruction.getDateExpiration() != null) {
                existingPermiConstruction.setDateExpiration(permiConstruction.getDateExpiration());
            }
            if (permiConstruction.getAutoriteDelivrance() != null) {
                existingPermiConstruction.setAutoriteDelivrance(permiConstruction.getAutoriteDelivrance());
            }
            if (permiConstruction.getSectionCategory() != null) {
                existingPermiConstruction.setSectionCategory(permiConstruction.getSectionCategory());
            }
            if (permiConstruction.getStatus() != null) {
                existingPermiConstruction.setStatus(permiConstruction.getStatus());
            }
            
            PermiConstruction savedPermiConstruction = permiConstructionRepository.save(existingPermiConstruction);
            return ResponseUtil.success(savedPermiConstruction, "Construction permit updated successfully");
        } catch (Exception e) {
            log.error("Error updating construction permit with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update construction permit: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete construction permit", description = "Soft delete a construction permit record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construction permit deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Construction permit not found")
    })
    public ResponseEntity<Map<String, Object>> deletePermiConstruction(@PathVariable Long id) {
        try {
            log.info("Deleting construction permit with ID: {}", id);
            
            Optional<PermiConstruction> permiConstructionOpt = permiConstructionRepository.findByIdAndActiveTrue(id);
            if (permiConstructionOpt.isEmpty()) {
                return ResponseUtil.badRequest("Construction permit not found with ID: " + id);
            }
            
            PermiConstruction permiConstruction = permiConstructionOpt.get();
            permiConstruction.setActive(false);
            permiConstructionRepository.save(permiConstruction);
            
            return ResponseUtil.success(null, "Construction permit deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting construction permit with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete construction permit: " + e.getMessage());
        }
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get expiring construction permits", description = "Retrieve construction permits expiring within specified days")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expiring construction permits retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<Map<String, Object>> getExpiringPermiConstruction(
            @Parameter(description = "Days until expiration") @RequestParam(defaultValue = "30") Integer days,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size) {
        try {
            log.info("Retrieving construction permits expiring within {} days", days);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, "dateExpiration", "asc");
            Page<PermiConstructionProjection> expiringPermits = 
                permiConstructionRepository.findExpiringWithinDaysProjections(days, pageable);
            
            return ResponseUtil.successWithPagination(expiringPermits);
        } catch (Exception e) {
            log.error("Error retrieving expiring construction permits", e);
            return ResponseUtil.badRequest("Failed to retrieve expiring construction permits: " + e.getMessage());
        }
    }

    // Helper method to convert entity to projection
    private PermiConstructionProjection convertToProjection(PermiConstruction permiConstruction) {
        return new PermiConstructionProjection() {
            @Override
            public Long getId() { return permiConstruction.getId(); }
            
            @Override
            public java.time.LocalDateTime getDateTime() { return permiConstruction.getDateTime(); }
            
            @Override
            public String getNumeroPermis() { return permiConstruction.getNumeroPermis(); }
            
            @Override
            public String getProjet() { return permiConstruction.getProjet(); }
            
            @Override
            public String getLocalisation() { return permiConstruction.getLocalisation(); }
            
            @Override
            public java.time.LocalDateTime getDateDelivrance() { return permiConstruction.getDateDelivrance(); }
            
            @Override
            public java.time.LocalDateTime getDateExpiration() { return permiConstruction.getDateExpiration(); }
            
            @Override
            public String getAutoriteDelivrance() { return permiConstruction.getAutoriteDelivrance(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return permiConstruction.getDocument().getId(); }
                    
                    @Override
                    public String getFileName() { return permiConstruction.getDocument().getFileName(); }
                    
                    @Override
                    public String getOriginalFileName() { return permiConstruction.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return permiConstruction.getStatus().getId(); }
                    
                    @Override
                    public String getName() { return permiConstruction.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return permiConstruction.getDoneBy().getId(); }
                    
                    @Override
                    public String getFullName() { return permiConstruction.getDoneBy().getFullName(); }
                    
                    @Override
                    public String getUsername() { return permiConstruction.getDoneBy().getUsername(); }
                };
            }
            
            @Override
            public SectionCategoryInfo getSectionCategory() {
                if (permiConstruction.getSectionCategory() == null) return null;
                return new SectionCategoryInfo() {
                    @Override
                    public Long getId() { return permiConstruction.getSectionCategory().getId(); }
                    
                    @Override
                    public String getName() { return permiConstruction.getSectionCategory().getName(); }
                };
            }
        };
    }
}