package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.SectionCategory;
import com.bar.gestiondesfichier.document.projection.SectionCategoryProjection;
import com.bar.gestiondesfichier.document.repository.SectionCategoryRepository;
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
 * REST controller for SectionCategory management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/section-category")
@DocumentControllerCors
@Tag(name = "Section Category Management", description = "Section Category CRUD operations with pagination")
@Slf4j
public class SectionCategoryController {

    private final SectionCategoryRepository sectionCategoryRepository;

    public SectionCategoryController(SectionCategoryRepository sectionCategoryRepository) {
        this.sectionCategoryRepository = sectionCategoryRepository;
    }

    @GetMapping
    @Operation(summary = "Get all section categories", description = "Retrieve paginated list of section categories with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section categories retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllSectionCategories(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        try {
            log.info("Retrieving section categories - page: {}, size: {}, sort: {} {}", page, size, sort, direction);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<SectionCategoryProjection> categories = sectionCategoryRepository.findAllActiveProjections(pageable);
            
            return ResponseUtil.successWithPagination(categories);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for section category retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving section categories", e);
            return ResponseUtil.badRequest("Failed to retrieve section categories: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get section category by ID", description = "Retrieve a specific section category by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section category retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Section category not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getSectionCategoryById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid section category ID");
            }
            
            log.info("Retrieving section category by ID: {}", id);
            Optional<SectionCategory> category = sectionCategoryRepository.findByIdAndActiveTrue(id);
            
            if (category.isPresent()) {
                return ResponseUtil.success(category.get(), "Section category retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Section category not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving section category with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve section category: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create section category", description = "Create a new section category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Section category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createSectionCategory(@RequestBody SectionCategory sectionCategory) {
        try {
            log.info("Creating new section category: {}", sectionCategory.getName());
            
            // Validate required fields
            if (sectionCategory.getName() == null || sectionCategory.getName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Section category name is required");
            }
            
            // Check if category with this name already exists
            if (sectionCategoryRepository.existsByNameAndActiveTrue(sectionCategory.getName())) {
                return ResponseUtil.badRequest("Section category with this name already exists");
            }
            
            sectionCategory.setActive(true);
            SectionCategory savedCategory = sectionCategoryRepository.save(sectionCategory);
            
            return ResponseUtil.success(savedCategory, "Section category created successfully");
        } catch (Exception e) {
            log.error("Error creating section category", e);
            return ResponseUtil.badRequest("Failed to create section category: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update section category", description = "Update an existing section category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section category updated successfully"),
        @ApiResponse(responseCode = "400", description = "Section category not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updateSectionCategory(@PathVariable Long id, @RequestBody SectionCategory sectionCategory) {
        try {
            log.info("Updating section category with ID: {}", id);
            
            Optional<SectionCategory> existingCategoryOpt = sectionCategoryRepository.findByIdAndActiveTrue(id);
            if (existingCategoryOpt.isEmpty()) {
                return ResponseUtil.badRequest("Section category not found with ID: " + id);
            }
            
            SectionCategory existingCategory = existingCategoryOpt.get();
            
            // Update fields
            if (sectionCategory.getName() != null) {
                existingCategory.setName(sectionCategory.getName());
            }
            if (sectionCategory.getDescription() != null) {
                existingCategory.setDescription(sectionCategory.getDescription());
            }
            
            SectionCategory savedCategory = sectionCategoryRepository.save(existingCategory);
            return ResponseUtil.success(savedCategory, "Section category updated successfully");
        } catch (Exception e) {
            log.error("Error updating section category with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update section category: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete section category", description = "Soft delete a section category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section category deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Section category not found")
    })
    public ResponseEntity<Map<String, Object>> deleteSectionCategory(@PathVariable Long id) {
        try {
            log.info("Deleting section category with ID: {}", id);
            
            Optional<SectionCategory> categoryOpt = sectionCategoryRepository.findByIdAndActiveTrue(id);
            if (categoryOpt.isEmpty()) {
                return ResponseUtil.badRequest("Section category not found with ID: " + id);
            }
            
            SectionCategory category = categoryOpt.get();
            category.setActive(false);
            sectionCategoryRepository.save(category);
            
            return ResponseUtil.success(null, "Section category deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting section category with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete section category: " + e.getMessage());
        }
    }
}