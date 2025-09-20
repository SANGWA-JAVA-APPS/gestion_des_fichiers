package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Insurance entity representing insurance policies and coverage
 */
@Entity
@Table(name = "insurance")
@Getter
@Setter
@Slf4j
public class Insurance extends DocumentRelatedEntity {

    @Column(name = "concerns", length = 1000)
    private String concerns;

    @Column(name = "coverage", length = 1000)
    private String coverage;

    @Column(name = "values", precision = 15, scale = 2)
    private BigDecimal values;

    @Column(name = "date_validity")
    private LocalDateTime dateValidity;

    @Column(name = "renewal_date")
    private LocalDateTime renewalDate;

    // Default constructor
    public Insurance() {
        super();
    }

    // Constructor with required fields
    public Insurance(Account doneBy, Document document, DocStatus status, String concerns) {
        super(doneBy, document, status);
        this.concerns = concerns;
    }
}