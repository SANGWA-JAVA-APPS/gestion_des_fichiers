package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Equipment ID entity representing equipment identification and tracking
 */
@Entity
@Table(name = "equipemt_id")
@Getter
@Setter
@Slf4j
public class EquipmentId extends DocumentRelatedEntity {

    @Column(name = "equipment_type", length = 100)
    private String equipmentType;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "plate_number", length = 100)
    private String plateNumber;

    @Column(name = "etat_equipement", length = 100)
    private String etatEquipement;

    @Column(name = "date_achat")
    private LocalDateTime dateAchat;

    @Column(name = "date_visite_technique")
    private LocalDateTime dateVisiteTechnique;

    @Column(name = "assurance", length = 500)
    private String assurance;

    @Column(name = "documents_telecharger", length = 500)
    private String documentsTelecharger;

    // Default constructor
    public EquipmentId() {
        super();
    }

    // Constructor with required fields
    public EquipmentId(Account doneBy, Document document, DocStatus status, String equipmentType) {
        super(doneBy, document, status);
        this.equipmentType = equipmentType;
    }
}