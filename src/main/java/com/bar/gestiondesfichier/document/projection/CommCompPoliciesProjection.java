package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * CommCompPolicies projection for commercial compliance policies
 */
public interface CommCompPoliciesProjection extends BaseDocumentRelatedProjection {
    String getPolicyName();
    String getRequirement();
    String getComplianceLevel();
    LocalDateTime getEffectiveDate();
    LocalDateTime getReviewDate();
}