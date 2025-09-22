package com.bar.gestiondesfichier.document.projection;

/**
 * Document summary projection for dashboard and statistics
 */
public interface DocumentSummaryProjection {
    Long getId();
    String getFileName();
    String getContentType();
    Long getFileSize();
    String getOwnerName();
    String getCreatedAt();
}