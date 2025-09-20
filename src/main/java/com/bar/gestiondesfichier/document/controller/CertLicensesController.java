package com.bar.gestiondesfichier.document.controller;

import com.bar.gestiondesfichier.common.annotation.DocumentControllerCors;
import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.document.model.CertLicenses;
import com.bar.gestiondesfichier.document.projection.CertLicensesProjection;
import com.bar.gestiondesfichier.document.repository.CertLicensesRepository;
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
 * REST controller for Certificates & Licenses management with 20-record default pagination
 */
@RestController
@RequestMapping("/api/document/cert-licenses")
@DocumentControllerCors
@Tag(name = "Certificates & Licenses Management", description = "Certificates & Licenses CRUD operations with pagination")
@Slf4j
public class CertLicensesController {

    private final CertLicensesRepository certLicensesRepository;

    public CertLicensesController(CertLicensesRepository certLicensesRepository) {
        this.certLicensesRepository = certLicensesRepository;
    }

    @GetMapping
    @Operation(summary = "Get all certificates & licenses", description = "Retrieve paginated list of certificates & licenses with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Certificates & licenses retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllCertLicenses(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "certificateName") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Filter by status ID") @RequestParam(required = false) Long statusId,
            @Parameter(description = "Filter by document ID") @RequestParam(required = false) Long documentId,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        try {
            log.info("Retrieving certificates & licenses - page: {}, size: {}, sort: {} {}, statusId: {}, search: {}", 
                    page, size, sort, direction, statusId, search);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<CertLicensesProjection> certLicenses;
            
            if (search != null && !search.trim().isEmpty()) {
                certLicenses = certLicensesRepository.findByActiveTrueAndCertificateNameOrIssuingAuthorityContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                certLicenses = certLicensesRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else if (documentId != null) {
                certLicenses = certLicensesRepository.findByActiveTrueAndDocument_Id(documentId, pageable)
                    .map(this::convertToProjection);
            } else {
                certLicenses = certLicensesRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(certLicenses);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for certificates & licenses retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving certificates & licenses", e);
            return ResponseUtil.badRequest("Failed to retrieve certificates & licenses: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get certificate & license by ID", description = "Retrieve a specific certificate & license record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Certificate & license retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Certificate & license not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getCertLicenseById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid certificate & license ID");
            }
            
            log.info("Retrieving certificate & license by ID: {}", id);
            Optional<CertLicenses> certLicense = certLicensesRepository.findByIdAndActiveTrue(id);
            
            if (certLicense.isPresent()) {
                return ResponseUtil.success(certLicense.get(), "Certificate & license retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Certificate & license not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving certificate & license with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve certificate & license: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create certificate & license", description = "Create a new certificate & license record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Certificate & license created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> createCertLicense(@RequestBody CertLicenses certLicense) {
        try {
            log.info("Creating new certificate & license: {}", certLicense.getCertificateName());
            
            // Validate required fields
            if (certLicense.getCertificateName() == null || certLicense.getCertificateName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Certificate name is required");
            }
            
            if (certLicense.getDoneBy() == null) {
                return ResponseUtil.badRequest("DoneBy (Account) is required");
            }
            
            if (certLicense.getDocument() == null) {
                return ResponseUtil.badRequest("Document is required");
            }
            
            if (certLicense.getStatus() == null) {
                return ResponseUtil.badRequest("Status is required");
            }
            
            certLicense.setActive(true);
            CertLicenses savedCertLicense = certLicensesRepository.save(certLicense);
            
            return ResponseUtil.success(savedCertLicense, "Certificate & license created successfully");
        } catch (Exception e) {
            log.error("Error creating certificate & license", e);
            return ResponseUtil.badRequest("Failed to create certificate & license: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update certificate & license", description = "Update an existing certificate & license record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Certificate & license updated successfully"),
        @ApiResponse(responseCode = "400", description = "Certificate & license not found or invalid data")
    })
    public ResponseEntity<Map<String, Object>> updateCertLicense(@PathVariable Long id, @RequestBody CertLicenses certLicense) {
        try {
            log.info("Updating certificate & license with ID: {}", id);
            
            Optional<CertLicenses> existingCertLicenseOpt = certLicensesRepository.findByIdAndActiveTrue(id);
            if (existingCertLicenseOpt.isEmpty()) {
                return ResponseUtil.badRequest("Certificate & license not found with ID: " + id);
            }
            
            CertLicenses existingCertLicense = existingCertLicenseOpt.get();
            
            // Update fields
            if (certLicense.getCertificateName() != null) {
                existingCertLicense.setCertificateName(certLicense.getCertificateName());
            }
            if (certLicense.getLicenseNumber() != null) {
                existingCertLicense.setLicenseNumber(certLicense.getLicenseNumber());
            }
            if (certLicense.getIssuingAuthority() != null) {
                existingCertLicense.setIssuingAuthority(certLicense.getIssuingAuthority());
            }
            if (certLicense.getIssueDate() != null) {
                existingCertLicense.setIssueDate(certLicense.getIssueDate());
            }
            if (certLicense.getExpiryDate() != null) {
                existingCertLicense.setExpiryDate(certLicense.getExpiryDate());
            }
            if (certLicense.getStatus() != null) {
                existingCertLicense.setStatus(certLicense.getStatus());
            }
            
            CertLicenses savedCertLicense = certLicensesRepository.save(existingCertLicense);
            return ResponseUtil.success(savedCertLicense, "Certificate & license updated successfully");
        } catch (Exception e) {
            log.error("Error updating certificate & license with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update certificate & license: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete certificate & license", description = "Soft delete a certificate & license record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Certificate & license deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Certificate & license not found")
    })
    public ResponseEntity<Map<String, Object>> deleteCertLicense(@PathVariable Long id) {
        try {
            log.info("Deleting certificate & license with ID: {}", id);
            
            Optional<CertLicenses> certLicenseOpt = certLicensesRepository.findByIdAndActiveTrue(id);
            if (certLicenseOpt.isEmpty()) {
                return ResponseUtil.badRequest("Certificate & license not found with ID: " + id);
            }
            
            CertLicenses certLicense = certLicenseOpt.get();
            certLicense.setActive(false);
            certLicensesRepository.save(certLicense);
            
            return ResponseUtil.success(null, "Certificate & license deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting certificate & license with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete certificate & license: " + e.getMessage());
        }
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get expiring certificates & licenses", description = "Retrieve certificates & licenses expiring within specified days")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expiring certificates & licenses retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<Map<String, Object>> getExpiringCertLicenses(
            @Parameter(description = "Days until expiration") @RequestParam(defaultValue = "30") Integer days,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size) {
        try {
            log.info("Retrieving certificates & licenses expiring within {} days", days);
            
            Pageable pageable = ResponseUtil.createPageable(page, size, "expiryDate", "asc");
            Page<CertLicensesProjection> expiringCertLicenses = 
                certLicensesRepository.findExpiringWithinDaysProjections(days, pageable);
            
            return ResponseUtil.successWithPagination(expiringCertLicenses);
        } catch (Exception e) {
            log.error("Error retrieving expiring certificates & licenses", e);
            return ResponseUtil.badRequest("Failed to retrieve expiring certificates & licenses: " + e.getMessage());
        }
    }

    // Helper method to convert entity to projection
    private CertLicensesProjection convertToProjection(CertLicenses certLicense) {
        return new CertLicensesProjection() {
            @Override
            public Long getId() { return certLicense.getId(); }
            
            @Override
            public java.time.LocalDateTime getDateTime() { return certLicense.getDateTime(); }
            
            @Override
            public String getCertificateName() { return certLicense.getCertificateName(); }
            
            @Override
            public String getLicenseNumber() { return certLicense.getLicenseNumber(); }
            
            @Override
            public String getIssuingAuthority() { return certLicense.getIssuingAuthority(); }
            
            @Override
            public java.time.LocalDateTime getIssueDate() { return certLicense.getIssueDate(); }
            
            @Override
            public java.time.LocalDateTime getExpiryDate() { return certLicense.getExpiryDate(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return certLicense.getDocument().getId(); }
                    
                    @Override
                    public String getFileName() { return certLicense.getDocument().getFileName(); }
                    
                    @Override
                    public String getOriginalFileName() { return certLicense.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return certLicense.getStatus().getId(); }
                    
                    @Override
                    public String getName() { return certLicense.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return certLicense.getDoneBy().getId(); }
                    
                    @Override
                    public String getFullName() { return certLicense.getDoneBy().getFullName(); }
                    
                    @Override
                    public String getUsername() { return certLicense.getDoneBy().getUsername(); }
                };
            }
        };
    }
}