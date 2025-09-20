package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.CommCompPolicies;
import com.bar.gestiondesfichier.document.projection.CommCompPoliciesProjection;
import com.bar.gestiondesfichier.document.repository.CommCompPoliciesRepository;
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
 * REST controller for Commercial Compliance Policies management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/comm-comp-policies")
@DocumentControllerCors
@Tag(name = "Commercial Compliance Policies Management", description = "Commercial Compliance Policies CRUD operations with pagination")
@Slf4j
public class CommCompPoliciesController {

    private final CommCompPoliciesRepository commCompPoliciesRepository;

    public CommCompPoliciesController(CommCompPoliciesRepository commCompPoliciesRepository) {
        this.commCompPoliciesRepository = commCompPoliciesRepository;
    }

    @GetMapping
    @Operation(summary = "Get all commercial compliance policies", description = "Retrieve paginated list of commercial compliance policies with default 20 records per page")
    public ResponseEntity<Map<String, Object>> getAllCommCompPolicies(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "policyName") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long documentId,
            @RequestParam(required = false) String search) {
        try {
            log.info("Retrieving commercial compliance policies - page: {}, size: {}, sort: {} {}", page, size, sort, direction);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<CommCompPoliciesProjection> policies;
            
            if (search != null && !search.trim().isEmpty()) {
                policies = commCompPoliciesRepository.findByActiveTrueAndPolicyNameOrRequirementContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                policies = commCompPoliciesRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else if (documentId != null) {
                policies = commCompPoliciesRepository.findByActiveTrueAndDocument_Id(documentId, pageable)
                    .map(this::convertToProjection);
            } else {
                policies = commCompPoliciesRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(policies);
        } catch (Exception e) {
            log.error("Error retrieving commercial compliance policies", e);
            return ResponseUtil.badRequest("Failed to retrieve commercial compliance policies: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCommCompPolicyById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid policy ID");
            }
            
            Optional<CommCompPolicies> policy = commCompPoliciesRepository.findByIdAndActiveTrue(id);
            
            if (policy.isPresent()) {
                return ResponseUtil.success(policy.get(), "Commercial compliance policy retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Commercial compliance policy not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving commercial compliance policy with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve commercial compliance policy: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCommCompPolicy(@RequestBody CommCompPolicies policy) {
        try {
            if (policy.getPolicyName() == null || policy.getPolicyName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Policy name is required");
            }
            if (policy.getDoneBy() == null || policy.getDocument() == null || policy.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            policy.setActive(true);
            CommCompPolicies savedPolicy = commCompPoliciesRepository.save(policy);
            return ResponseUtil.success(savedPolicy, "Commercial compliance policy created successfully");
        } catch (Exception e) {
            log.error("Error creating commercial compliance policy", e);
            return ResponseUtil.badRequest("Failed to create commercial compliance policy: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCommCompPolicy(@PathVariable Long id, @RequestBody CommCompPolicies policy) {
        try {
            Optional<CommCompPolicies> existingOpt = commCompPoliciesRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial compliance policy not found with ID: " + id);
            }
            
            CommCompPolicies existing = existingOpt.get();
            
            if (policy.getPolicyName() != null) existing.setPolicyName(policy.getPolicyName());
            if (policy.getRequirement() != null) existing.setRequirement(policy.getRequirement());
            if (policy.getComplianceLevel() != null) existing.setComplianceLevel(policy.getComplianceLevel());
            if (policy.getEffectiveDate() != null) existing.setEffectiveDate(policy.getEffectiveDate());
            if (policy.getReviewDate() != null) existing.setReviewDate(policy.getReviewDate());
            if (policy.getStatus() != null) existing.setStatus(policy.getStatus());
            
            CommCompPolicies savedPolicy = commCompPoliciesRepository.save(existing);
            return ResponseUtil.success(savedPolicy, "Commercial compliance policy updated successfully");
        } catch (Exception e) {
            log.error("Error updating commercial compliance policy with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update commercial compliance policy: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCommCompPolicy(@PathVariable Long id) {
        try {
            Optional<CommCompPolicies> policyOpt = commCompPoliciesRepository.findByIdAndActiveTrue(id);
            if (policyOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial compliance policy not found with ID: " + id);
            }
            
            CommCompPolicies policy = policyOpt.get();
            policy.setActive(false);
            commCompPoliciesRepository.save(policy);
            
            return ResponseUtil.success(null, "Commercial compliance policy deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting commercial compliance policy with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete commercial compliance policy: " + e.getMessage());
        }
    }

    private CommCompPoliciesProjection convertToProjection(CommCompPolicies policy) {
        return new CommCompPoliciesProjection() {
            @Override
            public Long getId() { return policy.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return policy.getDateTime(); }
            @Override
            public String getPolicyName() { return policy.getPolicyName(); }
            @Override
            public String getRequirement() { return policy.getRequirement(); }
            @Override
            public String getComplianceLevel() { return policy.getComplianceLevel(); }
            @Override
            public java.time.LocalDateTime getEffectiveDate() { return policy.getEffectiveDate(); }
            @Override
            public java.time.LocalDateTime getReviewDate() { return policy.getReviewDate(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return policy.getDocument().getId(); }
                    @Override
                    public String getFileName() { return policy.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return policy.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return policy.getStatus().getId(); }
                    @Override
                    public String getName() { return policy.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return policy.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return policy.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return policy.getDoneBy().getUsername(); }
                };
            }
        };
    }
}