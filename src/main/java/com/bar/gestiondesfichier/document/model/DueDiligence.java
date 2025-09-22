package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Due Diligence entity representing due diligence processes
 */
@Entity
@Table(name = "due_diligence")
@Getter
@Setter
@Slf4j
public class DueDiligence extends DocumentRelatedEntity {

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "date_due_diligence")
    private LocalDateTime dateDueDiligence;

    @Column(name = "auditor", length = 200)
    private String auditor;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "doc_attach", length = 500)
    private String docAttach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private SectionCategory section;

    // Default constructor
    public DueDiligence() {
        super();
        this.creationDate = LocalDateTime.now();
    }

    // Constructor with required fields
    public DueDiligence(Account doneBy, Document document, DocStatus status, String reference) {
        super(doneBy, document, status);
        this.reference = reference;
        this.creationDate = LocalDateTime.now();
    }
}