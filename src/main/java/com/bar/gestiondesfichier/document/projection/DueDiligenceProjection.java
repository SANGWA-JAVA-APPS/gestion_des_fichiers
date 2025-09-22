package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * DueDiligence projection for due diligence records
 */
public interface DueDiligenceProjection extends BaseDocumentRelatedProjection {
    String getProjectName();
    String getEntityName();
    String getScope();
    String getFindingsReport();
    String getRecommendations();
    LocalDateTime getCompletionDate();
}