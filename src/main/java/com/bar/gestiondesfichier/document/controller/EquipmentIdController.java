package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.*;
import com.bar.gestiondesfichier.document.projection.*;
import com.bar.gestiondesfichier.document.repository.*;
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

// ================ EQUIPMENT ID CONTROLLER ================

/**
 * REST controller for Equipment ID management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/equipment-id")
@DocumentControllerCors
@Tag(name = "Equipment ID Management", description = "Equipment ID CRUD operations with pagination")
@Slf4j
class EquipmentIdController {

    private final EquipmentIdRepository equipmentIdRepository;

    public EquipmentIdController(EquipmentIdRepository equipmentIdRepository) {
        this.equipmentIdRepository = equipmentIdRepository;
    }

    @GetMapping
    @Operation(summary = "Get all equipment IDs", description = "Retrieve paginated list of equipment IDs with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipment IDs retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllEquipmentId(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "equipmentName") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Filter by status ID") @RequestParam(required = false) Long statusId,
            @Parameter(description = "Filter by document ID") @RequestParam(required = false) Long documentId,
            @Parameter(description = "Filter by section category ID") @RequestParam(required = false) Long sectionCategoryId,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        try {
            log.info("Retrieving equipment IDs - page: {}, size: {}, sort: {} {}, statusId: {}, search: {}", 
                    page, size, sort, direction, statusId, search);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<EquipmentIdProjection> equipmentIds;
            
            if (search != null && !search.trim().isEmpty()) {
                equipmentIds = equipmentIdRepository.findByActiveTrueAndEquipmentNameOrLocationContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                equipmentIds = equipmentIdRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else if (sectionCategoryId != null) {
                equipmentIds = equipmentIdRepository.findByActiveTrueAndSectionCategory_Id(sectionCategoryId, pageable)
                    .map(this::convertToProjection);
            } else if (documentId != null) {
                equipmentIds = equipmentIdRepository.findByActiveTrueAndDocument_Id(documentId, pageable)
                    .map(this::convertToProjection);
            } else {
                equipmentIds = equipmentIdRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(equipmentIds);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for equipment ID retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving equipment IDs", e);
            return ResponseUtil.badRequest("Failed to retrieve equipment IDs: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get equipment ID by ID", description = "Retrieve a specific equipment ID record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipment ID retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Equipment ID not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getEquipmentIdById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid equipment ID");
            }
            
            log.info("Retrieving equipment ID by ID: {}", id);
            Optional<EquipmentId> equipmentId = equipmentIdRepository.findByIdAndActiveTrue(id);
            
            if (equipmentId.isPresent()) {
                return ResponseUtil.success(equipmentId.get(), "Equipment ID retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Equipment ID not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving equipment ID with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve equipment ID: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create equipment ID", description = "Create a new equipment ID record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Equipment ID created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createEquipmentId(@RequestBody EquipmentId equipmentId) {
        try {
            log.info("Creating new equipment ID: {}", equipmentId.getEquipmentName());
            
            // Validate required fields
            if (equipmentId.getEquipmentName() == null || equipmentId.getEquipmentName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Equipment name is required");
            }
            
            if (equipmentId.getDoneBy() == null) {
                return ResponseUtil.badRequest("DoneBy (Account) is required");
            }
            
            if (equipmentId.getDocument() == null) {
                return ResponseUtil.badRequest("Document is required");
            }
            
            if (equipmentId.getStatus() == null) {
                return ResponseUtil.badRequest("Status is required");
            }
            
            equipmentId.setActive(true);
            EquipmentId savedEquipmentId = equipmentIdRepository.save(equipmentId);
            
            return ResponseUtil.success(savedEquipmentId, "Equipment ID created successfully");
        } catch (Exception e) {
            log.error("Error creating equipment ID", e);
            return ResponseUtil.badRequest("Failed to create equipment ID: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update equipment ID", description = "Update an existing equipment ID record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipment ID updated successfully"),
        @ApiResponse(responseCode = "400", description = "Equipment ID not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updateEquipmentId(@PathVariable Long id, @RequestBody EquipmentId equipmentId) {
        try {
            log.info("Updating equipment ID with ID: {}", id);
            
            Optional<EquipmentId> existingEquipmentIdOpt = equipmentIdRepository.findByIdAndActiveTrue(id);
            if (existingEquipmentIdOpt.isEmpty()) {
                return ResponseUtil.badRequest("Equipment ID not found with ID: " + id);
            }
            
            EquipmentId existingEquipmentId = existingEquipmentIdOpt.get();
            
            // Update fields
            if (equipmentId.getEquipmentName() != null) {
                existingEquipmentId.setEquipmentName(equipmentId.getEquipmentName());
            }
            if (equipmentId.getSerialNumber() != null) {
                existingEquipmentId.setSerialNumber(equipmentId.getSerialNumber());
            }
            if (equipmentId.getManufacturer() != null) {
                existingEquipmentId.setManufacturer(equipmentId.getManufacturer());
            }
            if (equipmentId.getModel() != null) {
                existingEquipmentId.setModel(equipmentId.getModel());
            }
            if (equipmentId.getLocation() != null) {
                existingEquipmentId.setLocation(equipmentId.getLocation());
            }
            if (equipmentId.getSectionCategory() != null) {
                existingEquipmentId.setSectionCategory(equipmentId.getSectionCategory());
            }
            if (equipmentId.getStatus() != null) {
                existingEquipmentId.setStatus(equipmentId.getStatus());
            }
            
            EquipmentId savedEquipmentId = equipmentIdRepository.save(existingEquipmentId);
            return ResponseUtil.success(savedEquipmentId, "Equipment ID updated successfully");
        } catch (Exception e) {
            log.error("Error updating equipment ID with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update equipment ID: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete equipment ID", description = "Soft delete an equipment ID record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipment ID deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Equipment ID not found")
    })
    public ResponseEntity<Map<String, Object>> deleteEquipmentId(@PathVariable Long id) {
        try {
            log.info("Deleting equipment ID with ID: {}", id);
            
            Optional<EquipmentId> equipmentIdOpt = equipmentIdRepository.findByIdAndActiveTrue(id);
            if (equipmentIdOpt.isEmpty()) {
                return ResponseUtil.badRequest("Equipment ID not found with ID: " + id);
            }
            
            EquipmentId equipmentId = equipmentIdOpt.get();
            equipmentId.setActive(false);
            equipmentIdRepository.save(equipmentId);
            
            return ResponseUtil.success(null, "Equipment ID deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting equipment ID with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete equipment ID: " + e.getMessage());
        }
    }

    // Helper method to convert entity to projection
    private EquipmentIdProjection convertToProjection(EquipmentId equipmentId) {
        return new EquipmentIdProjection() {
            @Override
            public Long getId() { return equipmentId.getId(); }
            
            @Override
            public java.time.LocalDateTime getDateTime() { return equipmentId.getDateTime(); }
            
            @Override
            public String getEquipmentType() { return equipmentId.getEquipmentType(); }
            
            @Override
            public String getSerialNumber() { return equipmentId.getSerialNumber(); }
            
            @Override
            public String getPlateNumber() { return equipmentId.getPlateNumber(); }
            
            @Override
            public String getEtatEquipement() { return equipmentId.getEtatEquipement(); }
            
            @Override
            public java.time.LocalDateTime getDateAchat() { return equipmentId.getDateAchat(); }
            
            @Override
            public java.time.LocalDateTime getDateVisiteTechnique() { return equipmentId.getDateVisiteTechnique(); }
            
            @Override
            public String getAssurance() { return equipmentId.getAssurance(); }
            
            @Override
            public String getDocumentsTelecharger() { return equipmentId.getDocumentsTelecharger(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return equipmentId.getDocument().getId(); }
                    
                    @Override
                    public String getFileName() { return equipmentId.getDocument().getFileName(); }
                    
                    @Override
                    public String getOriginalFileName() { return equipmentId.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return equipmentId.getStatus().getId(); }
                    
                    @Override
                    public String getName() { return equipmentId.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return equipmentId.getDoneBy().getId(); }
                    
                    @Override
                    public String getFullName() { return equipmentId.getDoneBy().getFullName(); }
                    
                    @Override
                    public String getUsername() { return equipmentId.getDoneBy().getUsername(); }
                };
            }
            
            @Override
            public SectionCategoryInfo getSectionCategory() {
                if (equipmentId.getSectionCategory() == null) return null;
                return new SectionCategoryInfo() {
                    @Override
                    public Long getId() { return equipmentId.getSectionCategory().getId(); }
                    
                    @Override
                    public String getName() { return equipmentId.getSectionCategory().getName(); }
                };
            }
        };
    }
}