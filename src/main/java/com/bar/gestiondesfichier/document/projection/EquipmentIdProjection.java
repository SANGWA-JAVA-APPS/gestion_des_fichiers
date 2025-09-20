package com.bar.gestiondesfichier.document.projection;

import java.time.LocalDateTime;

/**
 * EquipmentId projection for equipment identification
 */
public interface EquipmentIdProjection extends BaseDocumentRelatedProjection {
    String getEquipmentType();
    String getSerialNumber();
    String getPlateNumber();
    String getEtatEquipement();
    LocalDateTime getDateAchat();
    LocalDateTime getDateVisiteTechnique();
    String getAssurance();
    String getDocumentsTelecharger();
}