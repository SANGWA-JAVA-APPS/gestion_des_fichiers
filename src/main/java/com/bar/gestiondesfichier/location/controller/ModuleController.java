package com.bar.gestiondesfichier.location.controller;

import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.location.model.Module;
import com.bar.gestiondesfichier.location.model.LocationEntity;
import com.bar.gestiondesfichier.location.repository.ModuleRepository;
import com.bar.gestiondesfichier.location.repository.LocationEntityRepository;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/location/modules")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
@Tag(name = "Module Management", description = "Module CRUD operations with pagination")
public class ModuleController {

    private static final Logger log = LoggerFactory.getLogger(ModuleController.class);
    private final ModuleRepository moduleRepository;
    private final LocationEntityRepository locationEntityRepository;

    public ModuleController(ModuleRepository moduleRepository, LocationEntityRepository locationEntityRepository) {
        this.moduleRepository = moduleRepository;
        this.locationEntityRepository = locationEntityRepository;
    }

    @GetMapping
    @Operation(summary = "Get all modules", description = "Retrieve paginated list of active modules with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modules retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllModules(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        try {
            log.info("Retrieving modules - page: {}, size: {}, sort: {} {}", page, size, sort, direction);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Module> modules = moduleRepository.findByActiveTrue(pageable);
            
            return ResponseUtil.successWithPagination(modules);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for module retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving modules", e);
            return ResponseUtil.badRequest("Failed to retrieve modules: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get module by ID", description = "Retrieve a specific module by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Module retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Module not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getModuleById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid module ID");
            }
            
            log.info("Retrieving module by ID: {}", id);
            Optional<Module> module = moduleRepository.findByIdAndActiveTrue(id);
            
            if (module.isPresent()) {
                return ResponseUtil.success(module.get(), "Module retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Module not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving module with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve module: " + e.getMessage());
        }
    }

    @GetMapping("/entity/{entityId}")
    @Operation(summary = "Get modules by location entity", description = "Retrieve modules for a specific location entity")
    public ResponseEntity<Map<String, Object>> getModulesByLocationEntity(
            @PathVariable Long entityId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            if (entityId == null || entityId <= 0) {
                return ResponseUtil.badRequest("Invalid location entity ID");
            }
            
            log.info("Retrieving modules by location entity: {}", entityId);
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Module> modules = moduleRepository.findByLocationEntityIdAndActiveTrue(entityId, pageable);
            
            return ResponseUtil.successWithPagination(modules);
        } catch (Exception e) {
            log.error("Error retrieving modules for location entity: {}", entityId, e);
            return ResponseUtil.badRequest("Failed to retrieve modules: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create module", description = "Create a new module")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Module created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> createModule(@RequestBody ModuleRequest moduleRequest) {
        try {
            log.info("Creating module: {}", moduleRequest.getName());
            
            // Validate input
            if (moduleRequest.getName() == null || moduleRequest.getName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Module name is required");
            }
            
            if (moduleRequest.getEntityId() == null || moduleRequest.getEntityId() <= 0) {
                return ResponseUtil.badRequest("Valid location entity ID is required");
            }
            
            // Find the location entity
            Optional<LocationEntity> locationEntity = locationEntityRepository.findByIdAndActiveTrue(moduleRequest.getEntityId());
            if (!locationEntity.isPresent()) {
                return ResponseUtil.badRequest("Location entity not found with ID: " + moduleRequest.getEntityId());
            }

            Module module = new Module();
            module.setName(moduleRequest.getName().trim());
            module.setDescription(moduleRequest.getDescription() != null ? moduleRequest.getDescription().trim() : null);
            module.setLocationEntity(locationEntity.get());
            module.setActive(true);

            Module savedModule = moduleRepository.save(module);
            return ResponseUtil.success(savedModule, "Module created successfully");
        } catch (Exception e) {
            log.error("Error creating module", e);
            return ResponseUtil.badRequest("Failed to create module: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update module", description = "Update an existing module")
    public ResponseEntity<Map<String, Object>> updateModule(@PathVariable Long id, @RequestBody ModuleRequest moduleRequest) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid module ID");
            }
            
            if (moduleRequest.getName() == null || moduleRequest.getName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Module name is required");
            }
            
            log.info("Updating module with ID: {}", id);
            
            Optional<Module> existingModule = moduleRepository.findByIdAndActiveTrue(id);
            if (!existingModule.isPresent()) {
                return ResponseUtil.badRequest("Module not found with ID: " + id);
            }

            Module module = existingModule.get();
            module.setName(moduleRequest.getName().trim());
            module.setDescription(moduleRequest.getDescription() != null ? moduleRequest.getDescription().trim() : null);

            if (moduleRequest.getEntityId() != null && moduleRequest.getEntityId() > 0) {
                Optional<LocationEntity> locationEntity = locationEntityRepository.findByIdAndActiveTrue(moduleRequest.getEntityId());
                if (locationEntity.isPresent()) {
                    module.setLocationEntity(locationEntity.get());
                } else {
                    return ResponseUtil.badRequest("Location entity not found with ID: " + moduleRequest.getEntityId());
                }
            }

            Module savedModule = moduleRepository.save(module);
            return ResponseUtil.success(savedModule, "Module updated successfully");
        } catch (Exception e) {
            log.error("Error updating module with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update module: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete module", description = "Soft delete a module")
    public ResponseEntity<Map<String, Object>> deleteModule(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid module ID");
            }
            
            log.info("Deleting module with ID: {}", id);
            
            Optional<Module> module = moduleRepository.findByIdAndActiveTrue(id);
            if (!module.isPresent()) {
                return ResponseUtil.badRequest("Module not found with ID: " + id);
            }

            Module moduleEntity = module.get();
            moduleEntity.setActive(false);
            moduleRepository.save(moduleEntity);
            
            return ResponseUtil.success(null, "Module deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting module with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete module: " + e.getMessage());
        }
    }

    // Simple DTO for simplified frontend requirements
    public static class ModuleRequest {
        private String name;
        private String description;
        private Long entityId;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Long getEntityId() { return entityId; }
        public void setEntityId(Long entityId) { this.entityId = entityId; }
    }
}