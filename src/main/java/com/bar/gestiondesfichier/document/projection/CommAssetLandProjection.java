package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * CommAssetLand projection for listing commercial asset land records
 */
public interface CommAssetLandProjection {
    Long getId();
    LocalDateTime getDateTime();
    String getDescription();
    String getReference();
    LocalDateTime getDateObtention();
    String getCoordonneesGps();
    String getEmplacement();
    
    // Document information
    DocumentInfo getDocument();
    
    // Status information
    StatusInfo getStatus();
    
    // Done by information
    DoneByInfo getDoneBy();
    
    // Section information
    SectionInfo getSection();
    
    interface DocumentInfo {
        Long getId();
        String getFileName();
    }
    
    interface StatusInfo {
        Long getId();
        String getName();
    }
    
    interface DoneByInfo {
        Long getId();
        String getFullName();
    }
    
    interface SectionInfo {
        Long getId();
        String getName();
    }
}