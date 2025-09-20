package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Commercial Follow-up Audit entity representing audit follow-ups
 */
@Entity
@Table(name = "comm_followup_audit")
@Getter
@Setter
@Slf4j
public class CommFollowupAudit extends DocumentRelatedEntity {

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "date_audit")
    private LocalDateTime dateAudit;

    @Column(name = "auditor", length = 200)
    private String auditor;

    @Column(name = "num_non_conform")
    private Integer numNonConform;

    @Column(name = "type_conform", length = 100)
    private String typeConform;

    @Column(name = "percent_complete", precision = 5, scale = 2)
    private BigDecimal percentComplete;

    @Column(name = "doc_attach", length = 500)
    private String docAttach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private SectionCategory section;

    // Default constructor
    public CommFollowupAudit() {
        super();
    }

    // Constructor with required fields
    public CommFollowupAudit(Account doneBy, Document document, DocStatus status, String reference) {
        super(doneBy, document, status);
        this.reference = reference;
    }
}