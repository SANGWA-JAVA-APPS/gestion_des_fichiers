package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * Estate projection for real estate properties
 */
public interface EstateProjection extends BaseDocumentRelatedProjection {
    String getReference();
    String getEstateType();
    String getEmplacement();
    String getCoordonneesGps();
    LocalDateTime getDateOfBuilding();
    String getComments();
}