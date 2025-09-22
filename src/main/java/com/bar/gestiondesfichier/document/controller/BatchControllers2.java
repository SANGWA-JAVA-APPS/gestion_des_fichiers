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

// ================ COMMERCIAL THIRD PARTY CONTROLLER ================

@RestController
@RequestMapping("/api/document/comm-third-party")
@DocumentControllerCors
@Tag(name = "Commercial Third Party Management", description = "Commercial Third Party CRUD operations with pagination")
class CommThirdPartyController {

    private static final Logger log = LoggerFactory.getLogger(CommThirdPartyController.class);
    private final CommThirdPartyRepository commThirdPartyRepository;

    public CommThirdPartyController(CommThirdPartyRepository commThirdPartyRepository) {
        this.commThirdPartyRepository = commThirdPartyRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCommThirdParty(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "thirdPartyName") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<CommThirdPartyProjection> thirdParties;
            
            if (search != null && !search.trim().isEmpty()) {
                thirdParties = commThirdPartyRepository.findByActiveTrueAndThirdPartyNameOrServiceTypeContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                thirdParties = commThirdPartyRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else {
                thirdParties = commThirdPartyRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(thirdParties);
        } catch (Exception e) {
            log.error("Error retrieving commercial third parties", e);
            return ResponseUtil.badRequest("Failed to retrieve commercial third parties: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCommThirdPartyById(@PathVariable Long id) {
        try {
            Optional<CommThirdParty> thirdParty = commThirdPartyRepository.findByIdAndActiveTrue(id);
            
            if (thirdParty.isPresent()) {
                return ResponseUtil.success(thirdParty.get(), "Commercial third party retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Commercial third party not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving commercial third party with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve commercial third party: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCommThirdParty(@RequestBody CommThirdParty thirdParty) {
        try {
            if (thirdParty.getThirdPartyName() == null || thirdParty.getThirdPartyName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Third party name is required");
            }
            if (thirdParty.getDoneBy() == null || thirdParty.getDocument() == null || thirdParty.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            thirdParty.setActive(true);
            CommThirdParty savedThirdParty = commThirdPartyRepository.save(thirdParty);
            return ResponseUtil.success(savedThirdParty, "Commercial third party created successfully");
        } catch (Exception e) {
            log.error("Error creating commercial third party", e);
            return ResponseUtil.badRequest("Failed to create commercial third party: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCommThirdParty(@PathVariable Long id, @RequestBody CommThirdParty thirdParty) {
        try {
            Optional<CommThirdParty> existingOpt = commThirdPartyRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial third party not found with ID: " + id);
            }
            
            CommThirdParty existing = existingOpt.get();
            
            if (thirdParty.getThirdPartyName() != null) existing.setThirdPartyName(thirdParty.getThirdPartyName());
            if (thirdParty.getServiceType() != null) existing.setServiceType(thirdParty.getServiceType());
            if (thirdParty.getContactInfo() != null) existing.setContactInfo(thirdParty.getContactInfo());
            if (thirdParty.getContractTerms() != null) existing.setContractTerms(thirdParty.getContractTerms());
            if (thirdParty.getServiceLevel() != null) existing.setServiceLevel(thirdParty.getServiceLevel());
            if (thirdParty.getStatus() != null) existing.setStatus(thirdParty.getStatus());
            
            CommThirdParty savedThirdParty = commThirdPartyRepository.save(existing);
            return ResponseUtil.success(savedThirdParty, "Commercial third party updated successfully");
        } catch (Exception e) {
            log.error("Error updating commercial third party with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update commercial third party: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCommThirdParty(@PathVariable Long id) {
        try {
            Optional<CommThirdParty> thirdPartyOpt = commThirdPartyRepository.findByIdAndActiveTrue(id);
            if (thirdPartyOpt.isEmpty()) {
                return ResponseUtil.badRequest("Commercial third party not found with ID: " + id);
            }
            
            CommThirdParty thirdParty = thirdPartyOpt.get();
            thirdParty.setActive(false);
            commThirdPartyRepository.save(thirdParty);
            
            return ResponseUtil.success(null, "Commercial third party deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting commercial third party with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete commercial third party: " + e.getMessage());
        }
    }

    private CommThirdPartyProjection convertToProjection(CommThirdParty thirdParty) {
        return new CommThirdPartyProjection() {
            @Override
            public Long getId() { return thirdParty.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return thirdParty.getDateTime(); }
            @Override
            public String getThirdPartyName() { return thirdParty.getName(); }
            @Override
            public String getServiceType() { return thirdParty.getActivities(); }
            @Override
            public String getContactInfo() { return thirdParty.getLocation(); }
            @Override
            public String getContractTerms() { return thirdParty.getValidity(); }
            @Override
            public String getServiceLevel() { return thirdParty.getActivities(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return thirdParty.getDocument().getId(); }
                    @Override
                    public String getFileName() { return thirdParty.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return thirdParty.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return thirdParty.getStatus().getId(); }
                    @Override
                    public String getName() { return thirdParty.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return thirdParty.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return thirdParty.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return thirdParty.getDoneBy().getUsername(); }
                };
            }
        };
    }
}

// ================ CARGO DAMAGE CONTROLLER ================

@RestController
@RequestMapping("/api/document/cargo-damage")
@DocumentControllerCors
@Tag(name = "Cargo Damage Management", description = "Cargo Damage CRUD operations with pagination")
class CargoDamageController {

    private static final Logger log = LoggerFactory.getLogger(CargoDamageController.class);
    private final CargoDamageRepository cargoDamageRepository;

    public CargoDamageController(CargoDamageRepository cargoDamageRepository) {
        this.cargoDamageRepository = cargoDamageRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCargoDamage(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "incidentId") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<CargoDamageProjection> cargoDamages;
            
            if (search != null && !search.trim().isEmpty()) {
                cargoDamages = cargoDamageRepository.findByActiveTrueAndIncidentIdOrCargoTypeContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                cargoDamages = cargoDamageRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else {
                cargoDamages = cargoDamageRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(cargoDamages);
        } catch (Exception e) {
            log.error("Error retrieving cargo damages", e);
            return ResponseUtil.badRequest("Failed to retrieve cargo damages: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCargoDamageById(@PathVariable Long id) {
        try {
            Optional<CargoDamage> cargoDamage = cargoDamageRepository.findByIdAndActiveTrue(id);
            
            if (cargoDamage.isPresent()) {
                return ResponseUtil.success(cargoDamage.get(), "Cargo damage retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Cargo damage not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving cargo damage with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve cargo damage: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCargoDamage(@RequestBody CargoDamage cargoDamage) {
        try {
            if (cargoDamage.getIncidentId() == null || cargoDamage.getIncidentId().trim().isEmpty()) {
                return ResponseUtil.badRequest("Incident ID is required");
            }
            if (cargoDamage.getDoneBy() == null || cargoDamage.getDocument() == null || cargoDamage.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            cargoDamage.setActive(true);
            CargoDamage savedCargoDamage = cargoDamageRepository.save(cargoDamage);
            return ResponseUtil.success(savedCargoDamage, "Cargo damage created successfully");
        } catch (Exception e) {
            log.error("Error creating cargo damage", e);
            return ResponseUtil.badRequest("Failed to create cargo damage: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCargoDamage(@PathVariable Long id, @RequestBody CargoDamage cargoDamage) {
        try {
            Optional<CargoDamage> existingOpt = cargoDamageRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Cargo damage not found with ID: " + id);
            }
            
            CargoDamage existing = existingOpt.get();
            
            if (cargoDamage.getIncidentId() != null) existing.setIncidentId(cargoDamage.getIncidentId());
            if (cargoDamage.getCargoType() != null) existing.setCargoType(cargoDamage.getCargoType());
            if (cargoDamage.getDamageDescription() != null) existing.setDamageDescription(cargoDamage.getDamageDescription());
            if (cargoDamage.getDamageAmount() != null) existing.setDamageAmount(cargoDamage.getDamageAmount());
            if (cargoDamage.getIncidentDate() != null) existing.setIncidentDate(cargoDamage.getIncidentDate());
            if (cargoDamage.getCarrier() != null) existing.setCarrier(cargoDamage.getCarrier());
            if (cargoDamage.getStatus() != null) existing.setStatus(cargoDamage.getStatus());
            
            CargoDamage savedCargoDamage = cargoDamageRepository.save(existing);
            return ResponseUtil.success(savedCargoDamage, "Cargo damage updated successfully");
        } catch (Exception e) {
            log.error("Error updating cargo damage with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update cargo damage: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCargoDamage(@PathVariable Long id) {
        try {
            Optional<CargoDamage> cargoDamageOpt = cargoDamageRepository.findByIdAndActiveTrue(id);
            if (cargoDamageOpt.isEmpty()) {
                return ResponseUtil.badRequest("Cargo damage not found with ID: " + id);
            }
            
            CargoDamage cargoDamage = cargoDamageOpt.get();
            cargoDamage.setActive(false);
            cargoDamageRepository.save(cargoDamage);
            
            return ResponseUtil.success(null, "Cargo damage deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting cargo damage with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete cargo damage: " + e.getMessage());
        }
    }

    private CargoDamageProjection convertToProjection(CargoDamage cargoDamage) {
        return new CargoDamageProjection() {
            @Override
            public Long getId() { return cargoDamage.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return cargoDamage.getDateTime(); }
            @Override
            public String getIncidentId() { return cargoDamage.getIncidentId(); }
            @Override
            public String getCargoType() { return cargoDamage.getCargoType(); }
            @Override
            public String getDamageDescription() { return cargoDamage.getDamageDescription(); }
            @Override
            public java.math.BigDecimal getDamageAmount() { return cargoDamage.getDamageAmount(); }
            @Override
            public java.time.LocalDateTime getIncidentDate() { return cargoDamage.getIncidentDate(); }
            @Override
            public String getCarrier() { return cargoDamage.getCarrier(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return cargoDamage.getDocument().getId(); }
                    @Override
                    public String getFileName() { return cargoDamage.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return cargoDamage.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return cargoDamage.getStatus().getId(); }
                    @Override
                    public String getName() { return cargoDamage.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return cargoDamage.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return cargoDamage.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return cargoDamage.getDoneBy().getUsername(); }
                };
            }
        };
    }
}

// ================ LITIGATION FOLLOWUP CONTROLLER ================

@RestController
@RequestMapping("/api/document/litigation-followup")
@DocumentControllerCors
@Tag(name = "Litigation Followup Management", description = "Litigation Followup CRUD operations with pagination")
class LitigationFollowupController {

    private static final Logger log = LoggerFactory.getLogger(LitigationFollowupController.class);
    private final LitigationFollowupRepository litigationFollowupRepository;

    public LitigationFollowupController(LitigationFollowupRepository litigationFollowupRepository) {
        this.litigationFollowupRepository = litigationFollowupRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLitigationFollowup(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "caseNumber") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<LitigationFollowupProjection> litigations;
            
            if (search != null && !search.trim().isEmpty()) {
                litigations = litigationFollowupRepository.findByActiveTrueAndCaseNumberOrCaseNameContaining(search, pageable)
                    .map(this::convertToProjection);
            } else if (statusId != null) {
                litigations = litigationFollowupRepository.findByActiveTrueAndStatus_Id(statusId, pageable)
                    .map(this::convertToProjection);
            } else {
                litigations = litigationFollowupRepository.findAllActiveProjections(pageable);
            }
            
            return ResponseUtil.successWithPagination(litigations);
        } catch (Exception e) {
            log.error("Error retrieving litigation followups", e);
            return ResponseUtil.badRequest("Failed to retrieve litigation followups: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLitigationFollowupById(@PathVariable Long id) {
        try {
            Optional<LitigationFollowup> litigation = litigationFollowupRepository.findByIdAndActiveTrue(id);
            
            if (litigation.isPresent()) {
                return ResponseUtil.success(litigation.get(), "Litigation followup retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Litigation followup not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving litigation followup with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve litigation followup: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createLitigationFollowup(@RequestBody LitigationFollowup litigation) {
        try {
            if (litigation.getCaseNumber() == null || litigation.getCaseNumber().trim().isEmpty()) {
                return ResponseUtil.badRequest("Case number is required");
            }
            if (litigation.getDoneBy() == null || litigation.getDocument() == null || litigation.getStatus() == null) {
                return ResponseUtil.badRequest("DoneBy, Document, and Status are required");
            }
            
            litigation.setActive(true);
            LitigationFollowup savedLitigation = litigationFollowupRepository.save(litigation);
            return ResponseUtil.success(savedLitigation, "Litigation followup created successfully");
        } catch (Exception e) {
            log.error("Error creating litigation followup", e);
            return ResponseUtil.badRequest("Failed to create litigation followup: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateLitigationFollowup(@PathVariable Long id, @RequestBody LitigationFollowup litigation) {
        try {
            Optional<LitigationFollowup> existingOpt = litigationFollowupRepository.findByIdAndActiveTrue(id);
            if (existingOpt.isEmpty()) {
                return ResponseUtil.badRequest("Litigation followup not found with ID: " + id);
            }
            
            LitigationFollowup existing = existingOpt.get();
            
            if (litigation.getCaseNumber() != null) existing.setCaseNumber(litigation.getCaseNumber());
            if (litigation.getCaseName() != null) existing.setCaseName(litigation.getCaseName());
            if (litigation.getPlaintiff() != null) existing.setPlaintiff(litigation.getPlaintiff());
            if (litigation.getDefendant() != null) existing.setDefendant(litigation.getDefendant());
            if (litigation.getCaseStatus() != null) existing.setCaseStatus(litigation.getCaseStatus());
            if (litigation.getNextHearingDate() != null) existing.setNextHearingDate(litigation.getNextHearingDate());
            if (litigation.getStatus() != null) existing.setStatus(litigation.getStatus());
            
            LitigationFollowup savedLitigation = litigationFollowupRepository.save(existing);
            return ResponseUtil.success(savedLitigation, "Litigation followup updated successfully");
        } catch (Exception e) {
            log.error("Error updating litigation followup with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to update litigation followup: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteLitigationFollowup(@PathVariable Long id) {
        try {
            Optional<LitigationFollowup> litigationOpt = litigationFollowupRepository.findByIdAndActiveTrue(id);
            if (litigationOpt.isEmpty()) {
                return ResponseUtil.badRequest("Litigation followup not found with ID: " + id);
            }
            
            LitigationFollowup litigation = litigationOpt.get();
            litigation.setActive(false);
            litigationFollowupRepository.save(litigation);
            
            return ResponseUtil.success(null, "Litigation followup deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting litigation followup with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to delete litigation followup: " + e.getMessage());
        }
    }

    private LitigationFollowupProjection convertToProjection(LitigationFollowup litigation) {
        return new LitigationFollowupProjection() {
            @Override
            public Long getId() { return litigation.getId(); }
            @Override
            public java.time.LocalDateTime getDateTime() { return litigation.getDateTime(); }
            @Override
            public String getCaseNumber() { return litigation.getCaseNumber(); }
            @Override
            public String getCaseName() { return litigation.getCaseName(); }
            @Override
            public String getPlaintiff() { return litigation.getPlaintiff(); }
            @Override
            public String getDefendant() { return litigation.getDefendant(); }
            @Override
            public String getCaseStatus() { return litigation.getCaseStatus(); }
            @Override
            public java.time.LocalDateTime getNextHearingDate() { return litigation.getNextHearingDate(); }
            
            @Override
            public DocumentInfo getDocument() {
                return new DocumentInfo() {
                    @Override
                    public Long getId() { return litigation.getDocument().getId(); }
                    @Override
                    public String getFileName() { return litigation.getDocument().getFileName(); }
                    @Override
                    public String getOriginalFileName() { return litigation.getDocument().getOriginalFileName(); }
                };
            }
            
            @Override
            public StatusInfo getStatus() {
                return new StatusInfo() {
                    @Override
                    public Long getId() { return litigation.getStatus().getId(); }
                    @Override
                    public String getName() { return litigation.getStatus().getName(); }
                };
            }
            
            @Override
            public DoneByInfo getDoneBy() {
                return new DoneByInfo() {
                    @Override
                    public Long getId() { return litigation.getDoneBy().getId(); }
                    @Override
                    public String getFullName() { return litigation.getDoneBy().getFullName(); }
                    @Override
                    public String getUsername() { return litigation.getDoneBy().getUsername(); }
                };
            }
        };
    }
}