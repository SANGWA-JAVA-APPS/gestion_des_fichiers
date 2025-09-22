package com.bar.gestiondesfichier.document.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ThirdPartyClaims projection for third party claims
 */
public interface ThirdPartyClaimsProjection extends BaseDocumentRelatedProjection {
    String getClaimNumber();
    String getClaimantName();
    String getClaimType();
    BigDecimal getClaimAmount();
    LocalDateTime getClaimDate();
    BigDecimal getSettlementAmount();
    String getClaimStatus();
}