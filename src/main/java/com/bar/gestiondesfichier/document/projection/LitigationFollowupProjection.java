package com.bar.gestiondesfichier.document.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * LitigationFollowup projection for litigation followup records
 */
public interface LitigationFollowupProjection extends BaseDocumentRelatedProjection {
    String getCaseNumber();
    String getCaseName();
    String getPlaintiff();
    String getDefendant();
    String getCaseStatus();
    LocalDateTime getNextHearingDate();
}