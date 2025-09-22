package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.common.entity.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Document status entity representing different states of documents
 * Default values: applicable, suspended, replaced, annulé, en_cours, acquis, 
 * vendu, transféré, litigieux, validé
 */
@Entity
@Table(name = "docstatus")
@Getter
@Setter
public class DocStatus extends NamedEntity {

    @Column(name = "description", length = 500)
    private String description;

    // Default constructor
    public DocStatus() {
        super();
    }

    // Constructor with name
    public DocStatus(String name) {
        super(name);
    }

    // Constructor with name and description
    public DocStatus(String name, String description) {
        super(name);
        this.description = description;
    }
}