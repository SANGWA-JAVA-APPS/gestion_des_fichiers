package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Accord Concession entity representing concession agreements
 */
@Entity
@Table(name = "accord_concession")
@Getter
@Setter
@Slf4j
public class AccordConcession extends DocumentRelatedEntity {

    @Column(name = "contrat_concession", length = 100)
    private String contratConcession;

    @Column(name = "numero_accord", length = 100)
    private String numeroAccord;

    @Column(name = "objet_concession", length = 500)
    private String objetConcession;

    @Column(name = "concessionnaire", length = 200)
    private String concessionnaire;

    @Column(name = "duree_annees")
    private Integer dureeAnnees;

    @Column(name = "conditions_financieres", length = 1000)
    private String conditionsFinancieres;

    @Column(name = "emplacement", length = 500)
    private String emplacement;

    @Column(name = "coordonnees_gps", length = 200)
    private String coordonneesGps;

    @Column(name = "rapport_transfert_gestion", length = 500)
    private String rapportTransfertGestion;

    @Column(name = "date_debut_concession")
    private LocalDateTime dateDebutConcession;

    @Column(name = "date_fin_concession")
    private LocalDateTime dateFinConcession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_category_id")
    private SectionCategory sectionCategory;

    // Default constructor
    public AccordConcession() {
        super();
    }

    // Constructor with required fields
    public AccordConcession(Account doneBy, Document document, DocStatus status, String contratConcession) {
        super(doneBy, document, status);
        this.contratConcession = contratConcession;
    }
}