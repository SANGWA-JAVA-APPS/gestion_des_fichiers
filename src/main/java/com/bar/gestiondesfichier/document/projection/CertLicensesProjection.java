package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * CertLicenses projection for certificates and licenses
 */
public interface CertLicensesProjection extends BaseDocumentRelatedProjection {
    String getCertificateName();
    String getLicenseNumber();
    String getIssuingAuthority();
    LocalDateTime getIssueDate();
    LocalDateTime getExpiryDate();
}