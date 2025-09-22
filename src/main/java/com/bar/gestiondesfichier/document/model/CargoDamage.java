package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Cargo Damage entity representing cargo damage claims
 */
@Entity
@Table(name = "cargo_damage")
@Getter
@Setter
@Slf4j
public class CargoDamage extends DocumentRelatedEntity {

    @Column(name = "refe_request", length = 100)
    private String refeRequest;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "quotation_contract_num", length = 100)
    private String quotationContractNum;

    @Column(name = "date_request")
    private LocalDateTime dateRequest;

    @Column(name = "date_contract")
    private LocalDateTime dateContract;

    // Default constructor
    public CargoDamage() {
        super();
    }

    // Constructor with required fields
    public CargoDamage(Account doneBy, Document document, DocStatus status, String refeRequest) {
        super(doneBy, document, status);
        this.refeRequest = refeRequest;
    }
}