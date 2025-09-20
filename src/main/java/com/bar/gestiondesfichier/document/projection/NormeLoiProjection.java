package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * NormeLoi projection for listing norms and laws
 */
public interface NormeLoiProjection {
    Long getId();
    LocalDateTime getDateTime();
    String getReference();
    String getDescription();
    LocalDateTime getDateVigueur();
    String getDomaineApplication();
    
    // Document information
    DocumentInfo getDocument();
    
    // Status information
    StatusInfo getStatus();
    
    // Done by information
    DoneByInfo getDoneBy();
    
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
}