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

// ================ INSURANCE CONTROLLER ================

@RestController
@RequestMapping("/api/document/insurance")
@DocumentControllerCors
@Tag(name = "Insurance Management", description = "Insurance CRUD operations with pagination")
@Slf4j
class InsuranceController {

    private final InsuranceRepository insuranceRepository;

    public InsuranceController(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllInsurance(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "policyNumber") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<InsuranceProjection> insurances;
            
            if (search != null && !search.trim().isEmpty()) {
                insurances = insuranceRepository.findByActiveTrueAndPolicyNumberOrInsuranceTypeContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                insurances = insuranceRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else {
                insurances = insuranceRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(insurances);
        } catch (Exception e) {
            log.error("Error retrieving insurances", e);
            return ResponseUtil.badRequest("Failed to retrieve insurances: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getInsuranceById(@PathVariable Long id) {
        try {
            Optional<Insurance> insurance = insuranceRepository.findByIdAndActiveTrue(id);
            
            if (insurance.isPresent()) {
                return ResponseUtil.success(insurance.get(), "Insurance retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Insurance not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving insurance with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve insurance: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createInsurance(@RequestBody Insurance insurance) {
        try {
            if (insurance.getPolicyNumber() == null || insurance.getPolicyNumber().trim().isEmpty()) {
                return ResponseUtil.badRequest("Policy number is required");
            }
            if (insurance.getDoneBy() == null || insurance.getDocument() == null || insurance.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            insurance.setActive(true);
            Insurance savedInsurance = insuranceRepository.save(insurance);
            return ResponseUtil.success(savedInsurance, "Insurance created successfully");
        } catch (Exception e) {
            log.error("Error creating insurance", e);
            return ResponseUtil.badRequest("Failed to create insurance: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateInsurance(@PathVariable Long id, @RequestBody Insurance insurance) {
        try {
            Optional<Insurance> existingOpt = insuranceRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Insurance not found with ID: " + id);
            }
            
            Insurance existing = existingOpt.get();
            
            if (insurance.getPolicyNumber() != null) existing.setPolicyNumber(insurance.getPolicyNumber());
            if (insurance.getInsuranceType() != null) existing.setInsuranceType(insurance.getInsuranceType());
            if (insurance.getInsuranceCompany() != null) existing.setInsuranceCompany(insurance.getInsuranceCompany());
            if (insurance.getCoverageAmount() != null) existing.setCoverageAmount(insurance.getCoverageAmount());
            if (insurance.getPremiumAmount() != null) existing.setPremiumAmount(insurance.getPremiumAmount());
            if (insurance.getStartDate() != null) existing.setStartDate(insurance.getStartDate());
            if (insurance.getEndDate() != null) existing.setEndDate(insurance.getEndDate());
            if (insurance.getStatus() != null) existing.setStatus(insurance.getStatus());
            
            Insurance savedInsurance = insuranceRepository.save(existing);
            return ResponseUtil.success(savedInsurance, "Insurance updated successfully");
        } catch (Exception e) {
            log.error("Error updating insurance with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update insurance: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteInsurance(@PathVariable Long id) {
        try {
            Optional<Insurance> insuranceOpt = insuranceRepository.findByIdAndActiveTrue(id);
            if (insuranceOpt.isEmpty()) {
                return ResponseUtil.badRequest("Insurance not found with ID: " + id);
            }
            
            Insurance insurance = insuranceOpt.get();
            insurance.setActive(false);
            insuranceRepository.save(insurance);
            
            return ResponseUtil.success(null, "Insurance deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting insurance with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete insurance: " + e.getMessage());
        }
    }

    @GetMapping("/expiring")
    public ResponseEntity<Map<String, Object>> getExpiringInsurance(
            @RequestParam(defaultValue = "30") Integer days,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, "endDate", "asc");
            Page<InsuranceProjection> expiringInsurance = 
                insuranceRepository.findExpiringWithinDaysProjections(days, pageable);
            
            return ResponseUtil.successWithPagination(expiringInsurance);
        } catch (Exception e) {
            log.error("Error retrieving expiring insurance", e);
            return ResponseUtil.badRequest("Failed to retrieve expiring insurance: " + e.getMessage());
        }
    }

    private InsuranceProjection convertToProjection(Insurance insurance) {
        return new InsuranceProjection() {
            @Override
            public Long getId() { return insurance.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return insurance.getDateTime(); }
            @Override
            public String getPolicyNumber() { return insurance.getPolicyNumber(); }
            @Override
            public String getInsuranceType() { return insurance.getInsuranceType(); }
            @Override
            public String getInsuranceCompany() { return insurance.getInsuranceCompany(); }
            @Override
            public java.math.BigDecimal getCoverageAmount() { return insurance.getCoverageAmount(); }
            @Override
            public java.math.BigDecimal getPremiumAmount() { return insurance.getPremiumAmount(); }
            @Override
            public java.time.LocalDateTime getStartDate() { return insurance.getStartDate(); }
            @Override
            public java.time.LocalDateTime getEndDate() { return insurance.getEndDate(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return insurance.getDocument().getId(); }
                    @Override
                    public String getFileName() { return insurance.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return insurance.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return insurance.getStatus().getId(); }
                    @Override
                    public String getName() { return insurance.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return insurance.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return insurance.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return insurance.getDoneBy().getUsername(); }
                };
            }
        };
    }
}

// ================ THIRD PARTY CLAIMS CONTROLLER ================

@RestController
@RequestMapping("/api/document/third-party-claims")
@DocumentControllerCors
@Tag(name = "Third Party Claims Management", description = "Third Party Claims CRUD operations with pagination")
@Slf4j
class ThirdPartyClaimsController {

    private final ThirdPartyClaimsRepository thirdPartyClaimsRepository;

    public ThirdPartyClaimsController(ThirdPartyClaimsRepository thirdPartyClaimsRepository) {
        this.thirdPartyClaimsRepository = thirdPartyClaimsRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllThirdPartyClaims(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "claimNumber") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<ThirdPartyClaimsProjection> claims;
            
            if (search != null && !search.trim().isEmpty()) {
                claims = thirdPartyClaimsRepository.findByActiveTrueAndClaimNumberOrClaimantNameContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                claims = thirdPartyClaimsRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else {
                claims = thirdPartyClaimsRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(claims);
        } catch (Exception e) {
            log.error("Error retrieving third party claims", e);
            return ResponseUtil.badRequest("Failed to retrieve third party claims: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getThirdPartyClaimById(@PathVariable Long id) {
        try {
            Optional<ThirdPartyClaims> claim = thirdPartyClaimsRepository.findByIdAndActiveTrue(id);
            
            if (claim.isPresent()) {
                return ResponseUtil.success(claim.get(), "Third party claim retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Third party claim not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving third party claim with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve third party claim: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createThirdPartyClaim(@RequestBody ThirdPartyClaims claim) {
        try {
            if (claim.getClaimNumber() == null || claim.getClaimNumber().trim().isEmpty()) {
                return ResponseUtil.badRequest("Claim number is required");
            }
            if (claim.getDoneBy() == null || claim.getDocument() == null || claim.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            claim.setActive(true);
            ThirdPartyClaims savedClaim = thirdPartyClaimsRepository.save(claim);
            return ResponseUtil.success(savedClaim, "Third party claim created successfully");
        } catch (Exception e) {
            log.error("Error creating third party claim", e);
            return ResponseUtil.badRequest("Failed to create third party claim: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateThirdPartyClaim(@PathVariable Long id, @RequestBody ThirdPartyClaims claim) {
        try {
            Optional<ThirdPartyClaims> existingOpt = thirdPartyClaimsRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Third party claim not found with ID: " + id);
            }
            
            ThirdPartyClaims existing = existingOpt.get();
            
            if (claim.getClaimNumber() != null) existing.setClaimNumber(claim.getClaimNumber());
            if (claim.getClaimantName() != null) existing.setClaimantName(claim.getClaimantName());
            if (claim.getClaimType() != null) existing.setClaimType(claim.getClaimType());
            if (claim.getClaimAmount() != null) existing.setClaimAmount(claim.getClaimAmount());
            if (claim.getClaimDate() != null) existing.setClaimDate(claim.getClaimDate());
            if (claim.getSettlementAmount() != null) existing.setSettlementAmount(claim.getSettlementAmount());
            if (claim.getClaimStatus() != null) existing.setClaimStatus(claim.getClaimStatus());
            if (claim.getStatus() != null) existing.setStatus(claim.getStatus());
            
            ThirdPartyClaims savedClaim = thirdPartyClaimsRepository.save(existing);
            return ResponseUtil.success(savedClaim, "Third party claim updated successfully");
        } catch (Exception e) {
            log.error("Error updating third party claim with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update third party claim: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteThirdPartyClaim(@PathVariable Long id) {
        try {
            Optional<ThirdPartyClaims> claimOpt = thirdPartyClaimsRepository.findByIdAndActiveTrue(id);
            if (claimOpt.isEmpty()) {
                return ResponseUtil.badRequest("Third party claim not found with ID: " + id);
            }
            
            ThirdPartyClaims claim = claimOpt.get();
            claim.setActive(false);
            thirdPartyClaimsRepository.save(claim);
            
            return ResponseUtil.success(null, "Third party claim deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting third party claim with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete third party claim: " + e.getMessage());
        }
    }

    @GetMapping("/by-status/{claimStatus}")
    public ResponseEntity<Map<String, Object>> getThirdPartyClaimsByStatus(
            @PathVariable String claimStatus,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, "claimDate", "desc");
            Page<ThirdPartyClaimsProjection> claims = 
                thirdPartyClaimsRepository.findAllByClaimStatusProjections(claimStatus, pageable);
            
            return ResponseUtil.successWithPagination(claims);
        } catch (Exception e) {
            log.error("Error retrieving third party claims by status: {}", claimStatus, e);
            return ResponseUtil.badRequest("Failed to retrieve third party claims by status: " + e.getMessage());
        }
    }

    private ThirdPartyClaimsProjection convertToProjection(ThirdPartyClaims claim) {
        return new ThirdPartyClaimsProjection() {
            @Override
            public Long getId() { return claim.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return claim.getDateTime(); }
            @Override
            public String getClaimNumber() { return claim.getClaimNumber(); }
            @Override
            public String getClaimantName() { return claim.getClaimantName(); }
            @Override
            public String getClaimType() { return claim.getClaimType(); }
            @Override
            public java.math.BigDecimal getClaimAmount() { return claim.getClaimAmount(); }
            @Override
            public java.time.LocalDateTime getClaimDate() { return claim.getClaimDate(); }
            @Override
            public java.math.BigDecimal getSettlementAmount() { return claim.getSettlementAmount(); }
            @Override
            public String getClaimStatus() { return claim.getClaimStatus(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return claim.getDocument().getId(); }
                    @Override
                    public String getFileName() { return claim.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return claim.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return claim.getStatus().getId(); }
                    @Override
                    public String getName() { return claim.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return claim.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return claim.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return claim.getDoneBy().getUsername(); }
                };
            }
        };
    }
}