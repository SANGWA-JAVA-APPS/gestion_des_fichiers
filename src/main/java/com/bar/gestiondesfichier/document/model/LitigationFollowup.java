package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Litigation Follow-up entity representing litigation tracking
 */
@Entity
@Table(name = "litigation_followup")
@Getter
@Setter
@Slf4j
public class LitigationFollowup extends DocumentRelatedEntity {

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "concern", length = 1000)
    private String concern;

    @Column(name = "statut", length = 100)
    private String statut;

    @Column(name = "expected_completion")
    private LocalDateTime expectedCompletion;

    @Column(name = "risk_value", precision = 15, scale = 2)
    private BigDecimal riskValue;

    // Default constructor
    public LitigationFollowup() {
        super();
        this.creationDate = LocalDateTime.now();
    }

    // Constructor with required fields
    public LitigationFollowup(Account doneBy, Document document, DocStatus status, String concern) {
        super(doneBy, document, status);
        this.concern = concern;
        this.creationDate = LocalDateTime.now();
    }
}