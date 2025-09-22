package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Commercial Asset Land entity representing land assets
 */
@Entity
@Table(name = "comm_asset_land")
@Getter
@Setter
@Slf4j
public class CommAssetLand extends DocumentRelatedEntity {

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "date_obtention")
    private LocalDateTime dateObtention;

    @Column(name = "coordonnees_gps", length = 200)
    private String coordonneesGps;

    @Column(name = "emplacement", length = 500)
    private String emplacement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private SectionCategory section;

    // Default constructor
    public CommAssetLand() {
        super();
    }

    // Constructor with required fields
    public CommAssetLand(Account doneBy, Document document, DocStatus status, String reference) {
        super(doneBy, document, status);
        this.reference = reference;
    }
}