package com.bar.gestiondesfichier.document.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CommFollowupAudit projection for audit follow-ups
 */
public interface CommFollowupAuditProjection extends BaseDocumentRelatedProjection {
    String getReference();
    String getDescription();
    LocalDateTime getDateAudit();
    String getAuditor();
    Integer getNumNonConform();
    String getTypeConform();
    BigDecimal getPercentComplete();
    String getDocAttach();
    
    SectionInfo getSection();
    
    interface SectionInfo {
        Long getId();
        String getName();
    }
}

/**
 * DueDiligence projection for due diligence processes
 */
interface DueDiligenceProjection extends BaseDocumentRelatedProjection {
    String getReference();
    String getDescription();
    LocalDateTime getDateDueDiligence();
    String getAuditor();
    LocalDateTime getCreationDate();
    LocalDateTime getCompletionDate();
    String getDocAttach();
    
    SectionInfo getSection();
    
    interface SectionInfo {
        Long getId();
        String getName();
    }
}

/**
 * CommThirdParty projection for third party relationships
 */
interface CommThirdPartyProjection extends BaseDocumentRelatedProjection {
    String getName();
    String getLocation();
    String getValidity();
    String getActivities();
    
    SectionInfo getSection();
    
    interface SectionInfo {
        Long getId();
        String getName();
    }
}