package com.bar.gestiondesfichier.location.controller;

import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.location.model.Section;
import com.bar.gestiondesfichier.location.model.Module;
import com.bar.gestiondesfichier.location.repository.SectionRepository;
import com.bar.gestiondesfichier.location.repository.ModuleRepository;
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

@RestController
@RequestMapping("/api/location/sections")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
@Tag(name = "Section Management", description = "Section CRUD operations with pagination")
public class SectionController {

    private static final Logger log = LoggerFactory.getLogger(SectionController.class);
    private final SectionRepository sectionRepository;
    private final ModuleRepository moduleRepository;

    public SectionController(SectionRepository sectionRepository, ModuleRepository moduleRepository) {
        this.sectionRepository = sectionRepository;
        this.moduleRepository = moduleRepository;
    }

    @GetMapping
    @Operation(summary = "Get all sections", description = "Retrieve paginated list of active sections with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sections retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllSections(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        try {
            log.info("Retrieving sections - page: {}, size: {}, sort: {} {}", page, size, sort, direction);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Section> sections = sectionRepository.findByActiveTrue(pageable);
            
            return ResponseUtil.successWithPagination(sections);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for section retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving sections", e);
            return ResponseUtil.badRequest("Failed to retrieve sections: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get section by ID", description = "Retrieve a specific section by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Section not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getSectionById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid section ID");
            }
            
            log.info("Retrieving section by ID: {}", id);
            Optional<Section> section = sectionRepository.findByIdAndActiveTrue(id);
            
            if (section.isPresent()) {
                return ResponseUtil.success(section.get(), "Section retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Section not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving section with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve section: " + e.getMessage());
        }
    }

    @GetMapping("/module/{moduleId}")
    @Operation(summary = "Get sections by module", description = "Retrieve sections for a specific module")
    public ResponseEntity<Map<String, Object>> getSectionsByModule(
            @PathVariable Long moduleId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            if (moduleId == null || moduleId <= 0) {
                return ResponseUtil.badRequest("Invalid module ID");
            }
            
            log.info("Retrieving sections by module: {}", moduleId);
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Section> sections = sectionRepository.findByModuleIdAndActiveTrue(moduleId, pageable);
            
            return ResponseUtil.successWithPagination(sections);
        } catch (Exception e) {
            log.error("Error retrieving sections for module: {}", moduleId, e);
            return ResponseUtil.badRequest("Failed to retrieve sections: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create section", description = "Create a new section")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> createSection(@RequestBody SectionRequest sectionRequest) {
        try {
            log.info("Creating section: {}", sectionRequest.getName());
            
            // Validate input
            if (sectionRequest.getName() == null || sectionRequest.getName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Section name is required");
            }
            
            if (sectionRequest.getModuleId() == null || sectionRequest.getModuleId() <= 0) {
                return ResponseUtil.badRequest("Valid module ID is required");
            }
            
            // Find the module
            Optional<Module> module = moduleRepository.findByIdAndActiveTrue(sectionRequest.getModuleId());
            if (!module.isPresent()) {
                return ResponseUtil.badRequest("Module not found with ID: " + sectionRequest.getModuleId());
            }

            Section section = new Section();
            section.setName(sectionRequest.getName().trim());
            section.setDescription(sectionRequest.getDescription() != null ? sectionRequest.getDescription().trim() : null);
            section.setModule(module.get());
            section.setActive(true);

            Section savedSection = sectionRepository.save(section);
            return ResponseUtil.success(savedSection, "Section created successfully");
        } catch (Exception e) {
            log.error("Error creating section", e);
            return ResponseUtil.badRequest("Failed to create section: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update section", description = "Update an existing section")
    public ResponseEntity<Map<String, Object>> updateSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid section ID");
            }
            
            if (sectionRequest.getName() == null || sectionRequest.getName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Section name is required");
            }
            
            log.info("Updating section with ID: {}", id);
            
            Optional<Section> existingSection = sectionRepository.findByIdAndActiveTrue(id);
            if (!existingSection.isPresent()) {
                return ResponseUtil.badRequest("Section not found with ID: " + id);
            }

            Section section = existingSection.get();
            section.setName(sectionRequest.getName().trim());
            section.setDescription(sectionRequest.getDescription() != null ? sectionRequest.getDescription().trim() : null);

            if (sectionRequest.getModuleId() != null && sectionRequest.getModuleId() > 0) {
                Optional<Module> module = moduleRepository.findByIdAndActiveTrue(sectionRequest.getModuleId());
                if (module.isPresent()) {
                    section.setModule(module.get());
                } else {
                    return ResponseUtil.badRequest("Module not found with ID: " + sectionRequest.getModuleId());
                }
            }

            Section savedSection = sectionRepository.save(section);
            return ResponseUtil.success(savedSection, "Section updated successfully");
        } catch (Exception e) {
            log.error("Error updating section with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update section: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete section", description = "Soft delete a section")
    public ResponseEntity<Map<String, Object>> deleteSection(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid section ID");
            }
            
            log.info("Deleting section with ID: {}", id);
            
            Optional<Section> section = sectionRepository.findByIdAndActiveTrue(id);
            if (!section.isPresent()) {
                return ResponseUtil.badRequest("Section not found with ID: " + id);
            }

            Section sectionEntity = section.get();
            sectionEntity.setActive(false);
            sectionRepository.save(sectionEntity);
            
            return ResponseUtil.success(null, "Section deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting section with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete section: " + e.getMessage());
        }
    }

    // Simple DTO for simplified frontend requirements
    public static class SectionRequest {
        private String name;
        private String description;
        private Long moduleId;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Long getModuleId() { return moduleId; }
        public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    }
}