package com.bar.gestiondesfichier.controller;

import com.bar.gestiondesfichier.entity.AccountCategory;
import com.bar.gestiondesfichier.repository.AccountCategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/account-categories")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
@Tag(name = "Account Category Management", description = "Account Category CRUD operations")
public class AccountCategoryController {

    private final AccountCategoryRepository accountCategoryRepository;

    // Explicit constructor for dependency injection
    public AccountCategoryController(AccountCategoryRepository accountCategoryRepository) {
        this.accountCategoryRepository = accountCategoryRepository;
    }

    @GetMapping
    @Operation(summary = "Get all account categories", description = "Retrieve all account categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account categories retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AccountCategoryDTO>> getAllAccountCategories() {
        try {
            List<AccountCategory> categories = accountCategoryRepository.findAll();
            List<AccountCategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .toList();
            return ResponseEntity.ok(categoryDTOs);
        } catch (Exception e) {
            log.error("Error retrieving account categories", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account category by ID", description = "Retrieve a specific account category by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account category retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Account category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountCategoryDTO> getAccountCategoryById(@PathVariable Long id) {
        try {
            Optional<AccountCategory> category = accountCategoryRepository.findById(id);
            return category.map(c -> ResponseEntity.ok(convertToDTO(c)))
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving account category with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Create account category", description = "Create a new account category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountCategoryDTO> createAccountCategory(@RequestBody AccountCategoryRequest categoryRequest) {
        try {
            // Check if name already exists
            if (accountCategoryRepository.existsByName(categoryRequest.getName())) {
                return ResponseEntity.badRequest().build();
            }

            AccountCategory category = new AccountCategory();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());

            AccountCategory savedCategory = accountCategoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedCategory));
        } catch (Exception e) {
            log.error("Error creating account category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account category", description = "Update an existing account category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account category updated successfully"),
        @ApiResponse(responseCode = "404", description = "Account category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountCategoryDTO> updateAccountCategory(@PathVariable Long id, @RequestBody AccountCategoryRequest categoryRequest) {
        try {
            Optional<AccountCategory> existingCategory = accountCategoryRepository.findById(id);
            if (!existingCategory.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            AccountCategory category = existingCategory.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());

            AccountCategory savedCategory = accountCategoryRepository.save(category);
            return ResponseEntity.ok(convertToDTO(savedCategory));
        } catch (Exception e) {
            log.error("Error updating account category with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account category", description = "Delete an account category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAccountCategory(@PathVariable Long id) {
        try {
            Optional<AccountCategory> category = accountCategoryRepository.findById(id);
            if (!category.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            accountCategoryRepository.delete(category.get());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting account category with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Simple DTO for response
    public static class AccountCategoryDTO {
        private Long id;
        private String name;
        private String description;

        public AccountCategoryDTO(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // Simple DTO for request
    public static class AccountCategoryRequest {
        private String name;
        private String description;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // Conversion method
    private AccountCategoryDTO convertToDTO(AccountCategory category) {
        return new AccountCategoryDTO(
            category.getId(),
            category.getName(),
            category.getDescription()
        );
    }
}