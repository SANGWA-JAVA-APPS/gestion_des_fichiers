package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Commercial Third Party entity representing third party relationships
 */
@Entity
@Table(name = "comm_third_party")
@Getter
@Setter
@Slf4j
public class CommThirdParty extends DocumentRelatedEntity {

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "validity", length = 100)
    private String validity;

    @Column(name = "activities", length = 1000)
    private String activities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private SectionCategory section;

    // Default constructor
    public CommThirdParty() {
        super();
    }

    // Constructor with required fields
    public CommThirdParty(Account doneBy, Document document, DocStatus status, String name) {
        super(doneBy, document, status);
        this.name = name;
    }
}