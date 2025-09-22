package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.entity.Category;
import com.igihecyubuntu.app.dto.projection.CategoryProjection;
import com.igihecyubuntu.app.service.CategoryService;
import com.igihecyubuntu.app.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category Management", description = "Operations for managing blog categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all categories from the database")
    public ResponseEntity<List<CategoryProjection>> getAllCategories() {
        try {
            List<CategoryProjection> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public ResponseEntity<CategoryProjection> getCategoryById(@PathVariable Long id) {
        try {
            Optional<CategoryProjection> category = categoryService.getCategoryById(id);
            return category.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create new category", description = "Create a new blog category")
    public ResponseEntity<Category> createCategory(@RequestBody Category categoryData) {
        try {
            Category newCategory = categoryService.createCategory(categoryData);
            return ResponseEntity.ok(newCategory);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category updateData) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, updateData);
            return ResponseEntity.ok(updatedCategory);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category by its ID")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{doneBy}")
    @Operation(summary = "Get categories by user", description = "Retrieve categories created by a specific user")
    public ResponseEntity<List<CategoryProjection>> getCategoriesByUser(@PathVariable Long doneBy) {
        try {
            List<CategoryProjection> categories = categoryService.getCategoriesByUser(doneBy);
            return ResponseEntity.ok(categories);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get category by name", description = "Retrieve a category by its name")
    public ResponseEntity<CategoryProjection> getCategoryByName(@PathVariable String name) {
        try {
            Optional<CategoryProjection> category = categoryService.getCategoryByName(name);
            return category.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats/usage")
    @Operation(summary = "Get category usage statistics", description = "Retrieve statistics about category usage")
    public ResponseEntity<List<Object[]>> getCategoryUsageStatistics() {
        try {
            List<Object[]> stats = categoryService.getCategoryUsageStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats/counts")
    @Operation(summary = "Get category counts by user", description = "Retrieve category counts grouped by user")
    public ResponseEntity<List<Object[]>> getCategoryCountsByUser() {
        try {
            List<Object[]> counts = categoryService.getCategoryCountsByUser();
            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}