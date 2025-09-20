package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Estate entity representing real estate properties
 */
@Entity
@Table(name = "estate")
@Getter
@Setter
@Slf4j
public class Estate extends DocumentRelatedEntity {

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "estate_type", length = 100)
    private String estateType;

    @Column(name = "emplacement", length = 500)
    private String emplacement;

    @Column(name = "coordonnees_gps", length = 200)
    private String coordonneesGps;

    @Column(name = "date_of_building")
    private LocalDateTime dateOfBuilding;

    @Column(name = "comments", length = 1000)
    private String comments;

    // Default constructor
    public Estate() {
        super();
    }

    // Constructor with required fields
    public Estate(Account doneBy, Document document, DocStatus status, String reference) {
        super(doneBy, document, status);
        this.reference = reference;
    }
}