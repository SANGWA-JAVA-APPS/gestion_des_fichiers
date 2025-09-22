package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Category;
import com.igihecyubuntu.app.dto.projection.CategoryProjection;
import com.igihecyubuntu.app.repository.CategoryRepository;
import com.igihecyubuntu.app.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryProjection> getAllCategories() {
        return categoryRepository.findAllProjectedBy();
    }

    public Optional<CategoryProjection> getCategoryById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid category ID");
        }
        return categoryRepository.findProjectedById(id);
    }

    public List<CategoryProjection> getCategoriesByUser(Long doneBy) {
        if (doneBy == null || doneBy <= 0) {
            throw new BadRequestException("Invalid user ID");
        }
        return categoryRepository.findByDoneByOrderByNameAsc(doneBy);
    }

    public Optional<CategoryProjection> getCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Category name is required");
        }
        return categoryRepository.findByName(name);
    }

    public Category createCategory(Category category) {
        validateCategory(category);
        
        // Check if category name already exists
        Optional<CategoryProjection> existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory.isPresent()) {
            throw new BadRequestException("Category name already exists");
        }
        
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid category ID");
        }
        
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Category not found"));
        
        validateCategory(category);
        
        // Check if new name conflicts with existing category (excluding current one)
        Optional<CategoryProjection> nameCheck = categoryRepository.findByName(category.getName());
        if (nameCheck.isPresent() && !nameCheck.get().getId().equals(id)) {
            throw new BadRequestException("Category name already exists");
        }
        
        existingCategory.setName(category.getName());
        existingCategory.setDoneBy(category.getDoneBy());
        
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid category ID");
        }
        
        if (!categoryRepository.existsById(id)) {
            throw new BadRequestException("Category not found");
        }
        
        categoryRepository.deleteById(id);
    }

    // Dashboard methods
    public List<Object[]> getCategoryUsageStatistics() {
        return categoryRepository.findCategoryUsageStatistics();
    }

    public List<Object[]> getCategoryCountsByUser() {
        return categoryRepository.findCategoryCountsByUser();
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new BadRequestException("Category data is required");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new BadRequestException("Category name is required");
        }
        if (category.getDoneBy() == null || category.getDoneBy() <= 0) {
            throw new BadRequestException("Valid user ID is required");
        }
    }
}