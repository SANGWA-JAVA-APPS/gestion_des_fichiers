package com.bar.gestiondesfichier.document.model;

import com.bar.gestiondesfichier.common.entity.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Section category entity representing different document categories
 * Default values: financial, procurement, hr, technical, IT, real Estate, 
 * Shareholders, legal, quality, HSE, equipment, drug and alcohol, 
 * incident news letter, SOP
 */
@Entity
@Table(name = "section_category")
@Getter
@Setter
public class SectionCategory extends NamedEntity {

    @Column(name = "description", length = 500)
    private String description;

    // Default constructor
    public SectionCategory() {
        super();
    }

    // Constructor with name
    public SectionCategory(String name) {
        super(name);
    }

    // Constructor with name and description
    public SectionCategory(String name, String description) {
        super(name);
        this.description = description;
    }
}