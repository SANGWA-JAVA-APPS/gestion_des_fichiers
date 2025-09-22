package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Norme Loi entity representing legal norms and laws
 */
@Entity
@Table(name = "norme_loi")
@Getter
@Setter
@Slf4j
public class NormeLoi extends DocumentRelatedEntity {

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "date_vigueur")
    private LocalDateTime dateVigueur;

    @Column(name = "domaine_application", length = 500)
    private String domaineApplication;

    // Default constructor
    public NormeLoi() {
        super();
    }

    // Constructor with required fields
    public NormeLoi(Account doneBy, Document document, DocStatus status, String reference) {
        super(doneBy, document, status);
        this.reference = reference;
    }
}