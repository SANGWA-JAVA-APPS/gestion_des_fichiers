package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * PermiConstruction projection for construction permits
 */
public interface PermiConstructionProjection extends BaseDocumentRelatedProjection {
    // New primary fields expected by controller
    String getNumeroPermis();
    String getProjet();
    String getLocalisation();
    LocalDateTime getDateDelivrance();
    LocalDateTime getDateExpiration();
    String getAutoriteDelivrance();
    
    // Section category information
    SectionCategoryInfo getSectionCategory();
    
    // Original fields for backward compatibility
    String getReferenceTitreFoncier();
    String getRefPermisConstuire();
    LocalDateTime getDateValidation();
    LocalDateTime getDateEstimeeTravaux();
}