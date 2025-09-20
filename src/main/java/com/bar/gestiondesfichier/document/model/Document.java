package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Document entity representing files in the system
 */
@Entity
@Table(name = "files")
@Getter
@Setter
public class Document {

    private static final Logger log = LoggerFactory.getLogger(Document.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @Column(nullable = false, name = "original_file_name")
    private String originalFileName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Account owner;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean active = true;

    // Default constructor
    public Document() {
    }

    // Constructor for creating new documents
    public Document(String fileName, String originalFileName, String contentType,
            Long fileSize, String filePath, Account owner, LocalDateTime expirationDate) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.owner = owner;
        this.expirationDate = expirationDate;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        log.debug("Creating new document: {}", fileName);
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        log.debug("Updating document: {}", fileName);
        updatedAt = LocalDateTime.now();
    }
}
