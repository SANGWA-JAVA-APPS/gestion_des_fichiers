package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Commercial Company Policies entity representing company policies
 */
@Entity
@Table(name = "comm_comp_policies")
@Getter
@Setter
@Slf4j
public class CommCompPolicies extends DocumentRelatedEntity {

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "version", length = 20)
    private String version;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private SectionCategory section;

    // Default constructor
    public CommCompPolicies() {
        super();
    }

    // Constructor with required fields
    public CommCompPolicies(Account doneBy, Document document, DocStatus docStatus, String reference) {
        super(doneBy, document, docStatus);
        this.reference = reference;
    }
}