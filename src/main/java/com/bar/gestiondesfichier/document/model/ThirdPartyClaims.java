package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Third Party Claims entity representing third party claims
 */
@Entity
@Table(name = "third_party_claims")
@Getter
@Setter
@Slf4j
public class ThirdPartyClaims extends DocumentRelatedEntity {

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "date_claim")
    private LocalDateTime dateClaim;

    @Column(name = "department_in_charge", length = 200)
    private String departmentInCharge;

    // Default constructor
    public ThirdPartyClaims() {
        super();
    }

    // Constructor with required fields
    public ThirdPartyClaims(Account doneBy, Document document, DocStatus status, String reference) {
        super(doneBy, document, status);
        this.reference = reference;
    }
}