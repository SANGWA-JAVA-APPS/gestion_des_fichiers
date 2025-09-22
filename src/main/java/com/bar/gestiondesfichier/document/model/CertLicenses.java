package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Certificates and Licenses entity representing certifications and licenses
 */
@Entity
@Table(name = "cert_licenses")
@Getter
@Setter
@Slf4j
public class CertLicenses extends DocumentRelatedEntity {

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "agent_certifica", length = 200)
    private String agentCertifica;

    @Column(name = "numero_agent", length = 100)
    private String numeroAgent;

    @Column(name = "date_certificate")
    private LocalDateTime dateCertificate;

    @Column(name = "duree_certificat")
    private Integer dureeCertificat; // Duration in months

    // Default constructor
    public CertLicenses() {
        super();
    }

    // Constructor with required fields
    public CertLicenses(Account doneBy, Document document, DocStatus status, String description) {
        super(doneBy, document, status);
        this.description = description;
    }
}