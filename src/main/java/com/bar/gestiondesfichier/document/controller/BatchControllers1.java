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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

// ================ COMMERCIAL FOLLOWUP AUDIT CONTROLLER ================

@RestController
@RequestMapping("/api/document/comm-followup-audit")
@DocumentControllerCors
@Tag(name = "Commercial Followup Audit Management", description = "Commercial Followup Audit CRUD operations with pagination")
class CommFollowupAuditController {

    private static final Logger log = LoggerFactory.getLogger(CommFollowupAuditController.class);
    private final CommFollowupAuditRepository commFollowupAuditRepository;

    public CommFollowupAuditController(CommFollowupAuditRepository commFollowupAuditRepository) {
        this.commFollowupAuditRepository = commFollowupAuditRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCommFollowupAudit(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "auditTitle") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<CommFollowupAuditProjection> audits;
            
            if (search != null && !search.trim().isEmpty()) {
                audits = commFollowupAuditRepository.findByActiveTrueAndAuditTitleOrFindingsContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                audits = commFollowupAuditRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else {
                audits = commFollowupAuditRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(audits);
        } catch (Exception e) {
            log.error("Error retrieving commercial followup audits", e);
            return ResponseUtil.badRequest("Failed to retrieve commercial followup audits: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCommFollowupAuditById(@PathVariable Long id) {
        try {
            Optional<CommFollowupAudit> audit = commFollowupAuditRepository.findByIdAndActiveTrue(id);
            
            if (audit.isPresent()) {
                return ResponseUtil.success(audit.get(), "Commercial followup audit retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Commercial followup audit not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving commercial followup audit with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve commercial followup audit: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCommFollowupAudit(@RequestBody CommFollowupAudit audit) {
        try {
            if (audit.getAuditTitle() == null || audit.getAuditTitle().trim().isEmpty()) {
                return ResponseUtil.badRequest("Audit title is required");
            }
            if (audit.getDoneBy() == null || audit.getDocument() == null || audit.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            audit.setActive(true);
            CommFollowupAudit savedAudit = commFollowupAuditRepository.save(audit);
            return ResponseUtil.success(savedAudit, "Commercial followup audit created successfully");
        } catch (Exception e) {
            log.error("Error creating commercial followup audit", e);
            return ResponseUtil.badRequest("Failed to create commercial followup audit: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCommFollowupAudit(@PathVariable Long id, @RequestBody CommFollowupAudit audit) {
        try {
            Optional<CommFollowupAudit> existingOpt = commFollowupAuditRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial followup audit not found with ID: " + id);
            }
            
            CommFollowupAudit existing = existingOpt.get();
            
            if (audit.getAuditTitle() != null) existing.setAuditTitle(audit.getAuditTitle());
            if (audit.getAuditDate() != null) existing.setAuditDate(audit.getAuditDate());
            if (audit.getAuditor() != null) existing.setAuditor(audit.getAuditor());
            if (audit.getFindings() != null) existing.setFindings(audit.getFindings());
            if (audit.getRecommendations() != null) existing.setRecommendations(audit.getRecommendations());
            if (audit.getFollowupDate() != null) existing.setFollowupDate(audit.getFollowupDate());
            if (audit.getStatus() != null) existing.setStatus(audit.getStatus());
            
            CommFollowupAudit savedAudit = commFollowupAuditRepository.save(existing);
            return ResponseUtil.success(savedAudit, "Commercial followup audit updated successfully");
        } catch (Exception e) {
            log.error("Error updating commercial followup audit with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update commercial followup audit: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCommFollowupAudit(@PathVariable Long id) {
        try {
            Optional<CommFollowupAudit> auditOpt = commFollowupAuditRepository.findByIdAndActiveTrue(id);
            if (auditOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial followup audit not found with ID: " + id);
            }
            
            CommFollowupAudit audit = auditOpt.get();
            audit.setActive(false);
            commFollowupAuditRepository.save(audit);
            
            return ResponseUtil.success(null, "Commercial followup audit deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting commercial followup audit with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete commercial followup audit: " + e.getMessage());
        }
    }

    private CommFollowupAuditProjection convertToProjection(CommFollowupAudit audit) {
        return new CommFollowupAuditProjection() {
            @Override
            public Long getId() { return audit.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return audit.getDateTime(); }
            @Override
            public String getAuditTitle() { return audit.getAuditTitle(); }
            @Override
            public java.time.LocalDateTime getAuditDate() { return audit.getAuditDate(); }
            @Override
            public String getAuditor() { return audit.getAuditor(); }
            @Override
            public String getFindings() { return audit.getFindings(); }
            @Override
            public String getRecommendations() { return audit.getRecommendations(); }
            @Override
            public java.time.LocalDateTime getFollowupDate() { return audit.getFollowupDate(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return audit.getDocument().getId(); }
                    @Override
                    public String getFileName() { return audit.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return audit.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return audit.getStatus().getId(); }
                    @Override
                    public String getName() { return audit.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return audit.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return audit.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return audit.getDoneBy().getUsername(); }
                };
            }
        };
    }
}

// ================ DUE DILIGENCE CONTROLLER ================

@RestController
@RequestMapping("/api/document/due-diligence")
@DocumentControllerCors
@Tag(name = "Due Diligence Management", description = "Due Diligence CRUD operations with pagination")
class DueDiligenceController {

    private static final Logger log = LoggerFactory.getLogger(DueDiligenceController.class);
    private final DueDiligenceRepository dueDiligenceRepository;

    public DueDiligenceController(DueDiligenceRepository dueDiligenceRepository) {
        this.dueDiligenceRepository = dueDiligenceRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDueDiligence(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "projectName") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<DueDiligenceProjection> dueDiligences;
            
            if (search != null && !search.trim().isEmpty()) {
                dueDiligences = dueDiligenceRepository.findByActiveTrueAndProjectNameOrScopeContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                dueDiligences = dueDiligenceRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else {
                dueDiligences = dueDiligenceRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(dueDiligences);
        } catch (Exception e) {
            log.error("Error retrieving due diligences", e);
            return ResponseUtil.badRequest("Failed to retrieve due diligences: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDueDiligenceById(@PathVariable Long id) {
        try {
            Optional<DueDiligence> dueDiligence = dueDiligenceRepository.findByIdAndActiveTrue(id);
            
            if (dueDiligence.isPresent()) {
                return ResponseUtil.success(dueDiligence.get(), "Due diligence retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Due diligence not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving due diligence with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve due diligence: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDueDiligence(@RequestBody DueDiligence dueDiligence) {
        try {
            if (dueDiligence.getProjectName() == null || dueDiligence.getProjectName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Project name is required");
            }
            if (dueDiligence.getDoneBy() == null || dueDiligence.getDocument() == null || dueDiligence.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            dueDiligence.setActive(true);
            DueDiligence savedDueDiligence = dueDiligenceRepository.save(dueDiligence);
            return ResponseUtil.success(savedDueDiligence, "Due diligence created successfully");
        } catch (Exception e) {
            log.error("Error creating due diligence", e);
            return ResponseUtil.badRequest("Failed to create due diligence: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDueDiligence(@PathVariable Long id, @RequestBody DueDiligence dueDiligence) {
        try {
            Optional<DueDiligence> existingOpt = dueDiligenceRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Due diligence not found with ID: " + id);
            }
            
            DueDiligence existing = existingOpt.get();
            
            if (dueDiligence.getProjectName() != null) existing.setProjectName(dueDiligence.getProjectName());
            if (dueDiligence.getEntityName() != null) existing.setEntityName(dueDiligence.getEntityName());
            if (dueDiligence.getScope() != null) existing.setScope(dueDiligence.getScope());
            if (dueDiligence.getFindingsReport() != null) existing.setFindingsReport(dueDiligence.getFindingsReport());
            if (dueDiligence.getRecommendations() != null) existing.setRecommendations(dueDiligence.getRecommendations());
            if (dueDiligence.getCompletionDate() != null) existing.setCompletionDate(dueDiligence.getCompletionDate());
            if (dueDiligence.getStatus() != null) existing.setStatus(dueDiligence.getStatus());
            
            DueDiligence savedDueDiligence = dueDiligenceRepository.save(existing);
            return ResponseUtil.success(savedDueDiligence, "Due diligence updated successfully");
        } catch (Exception e) {
            log.error("Error updating due diligence with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update due diligence: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDueDiligence(@PathVariable Long id) {
        try {
            Optional<DueDiligence> dueDiligenceOpt = dueDiligenceRepository.findByIdAndActiveTrue(id);
            if (dueDiligenceOpt.isEmpty()) {
                return ResponseUtil.badRequest("Due diligence not found with ID: " + id);
            }
            
            DueDiligence dueDiligence = dueDiligenceOpt.get();
            dueDiligence.setActive(false);
            dueDiligenceRepository.save(dueDiligence);
            
            return ResponseUtil.success(null, "Due diligence deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting due diligence with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete due diligence: " + e.getMessage());
        }
    }

    private DueDiligenceProjection convertToProjection(DueDiligence dueDiligence) {
        return new DueDiligenceProjection() {
            @Override
            public Long getId() { return dueDiligence.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return dueDiligence.getDateTime(); }
            @Override
            public String getProjectName() { return dueDiligence.getProjectName(); }
            @Override
            public String getEntityName() { return dueDiligence.getEntityName(); }
            @Override
            public String getScope() { return dueDiligence.getScope(); }
            @Override
            public String getFindingsReport() { return dueDiligence.getFindingsReport(); }
            @Override
            public String getRecommendations() { return dueDiligence.getRecommendations(); }
            @Override
            public java.time.LocalDateTime getCompletionDate() { return dueDiligence.getCompletionDate(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return dueDiligence.getDocument().getId(); }
                    @Override
                    public String getFileName() { return dueDiligence.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return dueDiligence.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return dueDiligence.getStatus().getId(); }
                    @Override
                    public String getName() { return dueDiligence.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return dueDiligence.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return dueDiligence.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return dueDiligence.getDoneBy().getUsername(); }
                };
            }
        };
    }
}