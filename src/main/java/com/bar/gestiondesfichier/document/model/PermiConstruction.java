package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Permit Construction entity representing construction permits
 */
@Entity
@Table(name = "permi_construction")
@Getter
@Setter
@Slf4j
public class PermiConstruction extends DocumentRelatedEntity {

    @Column(name = "numero_permis", length = 100)
    private String numeroPermis;

    @Column(name = "projet", length = 500)
    private String projet;

    @Column(name = "localisation", length = 500)
    private String localisation;

    @Column(name = "date_delivrance")
    private LocalDateTime dateDelivrance;

    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;

    @Column(name = "autorite_delivrance", length = 200)
    private String autoriteDelivrance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_category_id")
    private SectionCategory sectionCategory;

    // Keep original fields for backward compatibility
    @Column(name = "reference_titre_foncier", length = 100)
    private String referenceTitreFoncier;

    @Column(name = "refe_permis_construire", length = 100)
    private String refPermisConstuire;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "date_estimee_travaux")
    private LocalDateTime dateEstimeeTravaux;

    // Default constructor
    public PermiConstruction() {
        super();
    }

    // Constructor with required fields
    public PermiConstruction(Account doneBy, Document document, DocStatus status, String numeroPermis) {
        super(doneBy, document, status);
        this.numeroPermis = numeroPermis;
    }
}