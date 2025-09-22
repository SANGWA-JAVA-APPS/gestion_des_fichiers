package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * AccordConcession projection for concession agreements
 */
public interface AccordConcessionProjection extends BaseDocumentRelatedProjection {
    String getContratConcession();
    String getNumeroAccord();
    String getObjetConcession();
    String getConcessionnaire();
    Integer getDureeAnnees();
    String getConditionsFinancieres();
    String getEmplacement();
    String getCoordonneesGps();
    String getRapportTransfertGestion();
    LocalDateTime getDateDebutConcession();
    LocalDateTime getDateFinConcession();
    SectionCategoryInfo getSectionCategory();
}