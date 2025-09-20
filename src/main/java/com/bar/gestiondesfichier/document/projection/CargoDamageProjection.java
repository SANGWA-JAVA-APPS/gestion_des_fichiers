package com.bar.gestiondesfichier.document.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CargoDamage projection for cargo damage claims
 */
public interface CargoDamageProjection extends BaseDocumentRelatedProjection {
    String getRefeRequest();
    String getDescription();
    String getQuotationContractNum();
    LocalDateTime getDateRequest();
    LocalDateTime getDateContract();
}

/**
 * LitigationFollowup projection for litigation tracking
 */
interface LitigationFollowupProjection extends BaseDocumentRelatedProjection {
    LocalDateTime getCreationDate();
    String getConcern();
    String getStatut();
    LocalDateTime getExpectedCompletion();
    BigDecimal getRiskValue();
}

/**
 * Insurance projection for insurance policies
 */
interface InsuranceProjection extends BaseDocumentRelatedProjection {
    String getConcerns();
    String getCoverage();
    BigDecimal getValues();
    LocalDateTime getDateValidity();
    LocalDateTime getRenewalDate();
}

/**
 * ThirdPartyClaims projection for third party claims
 */
interface ThirdPartyClaimsProjection extends BaseDocumentRelatedProjection {
    String getReference();
    String getDescription();
    LocalDateTime getDateClaim();
    String getDepartmentInCharge();
}