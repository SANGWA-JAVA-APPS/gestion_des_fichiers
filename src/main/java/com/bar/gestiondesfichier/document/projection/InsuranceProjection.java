package com.bar.gestiondesfichier.document.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Insurance projection for insurance records
 */
public interface InsuranceProjection extends BaseDocumentRelatedProjection {
    String getPolicyNumber();
    String getInsuranceType();
    String getInsuranceCompany();
    BigDecimal getCoverageAmount();
    BigDecimal getPremiumAmount();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}