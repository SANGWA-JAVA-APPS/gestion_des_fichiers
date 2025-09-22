package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.common.entity.BaseEntity;
import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Base class for all document-related entities
 * Contains common fields: date_time, doneby, docId
 */
@MappedSuperclass
@Getter
@Setter
public abstract class DocumentRelatedEntity extends BaseEntity {

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doneby", nullable = false)
    private Account doneBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statut_id", nullable = false)
    private DocStatus status;

    // Default constructor
    public DocumentRelatedEntity() {
        super();
        this.dateTime = LocalDateTime.now();
    }

    // Constructor with required fields
    public DocumentRelatedEntity(Account doneBy, Document document, DocStatus status) {
        this();
        this.doneBy = doneBy;
        this.document = document;
        this.status = status;
    }
}