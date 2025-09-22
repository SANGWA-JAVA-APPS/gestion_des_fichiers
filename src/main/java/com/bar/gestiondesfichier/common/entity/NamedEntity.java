package com.bar.gestiondesfichier.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class NamedEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    public NamedEntity() {
        super();
    }

    public NamedEntity(String name) {
        this();
        this.name = name;
    }

    public NamedEntity(String name, String description) {
        this(name);
        this.description = description;
    }
}