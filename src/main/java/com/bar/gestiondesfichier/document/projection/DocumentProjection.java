package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * Document projection for listing documents with basic information
 */
public interface DocumentProjection {
    Long getId();
    String getFileName();
    String getOriginalFileName();
    String getContentType();
    Long getFileSize();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    boolean isActive();
    
    // Owner information
    OwnerInfo getOwner();
    
    interface OwnerInfo {
        Long getId();
        String getUsername();
        String getFullName();
    }
}