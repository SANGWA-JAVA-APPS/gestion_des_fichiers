package com.bar.gestiondesfichier.location.model;

import com.bar.gestiondesfichier.common.entity.NamedEntity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "countries")
public class Country extends NamedEntity {

    @Column(name = "iso_code", unique = true, length = 3)
    private String isoCode;

    @Column(name = "phone_code", length = 10)
    private String phoneCode;

    @Column(name = "flag_url")
    private String flagUrl;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("country-entities")
    private List<LocationEntity> entities;

    // Default constructor
    public Country() {
        super();
    }

    // Constructor with all fields
    public Country(String name, String description, String isoCode, String phoneCode, String flagUrl) {
        super();
        setName(name);
        setDescription(description);
        this.isoCode = isoCode;
        this.phoneCode = phoneCode;
        this.flagUrl = flagUrl;
        setActive(true);
    }

    // Getters
    public String getIsoCode() { return isoCode; }
    public String getPhoneCode() { return phoneCode; }
    public String getFlagUrl() { return flagUrl; }
    public List<LocationEntity> getEntities() { return entities; }

    // Setters
    public void setIsoCode(String isoCode) { this.isoCode = isoCode; }
    public void setPhoneCode(String phoneCode) { this.phoneCode = phoneCode; }
    public void setFlagUrl(String flagUrl) { this.flagUrl = flagUrl; }
    public void setEntities(List<LocationEntity> entities) { this.entities = entities; }

    @PrePersist
    @PreUpdate
    private void validateIsoCode() {
        if (isoCode != null) {
            isoCode = isoCode.toUpperCase();
        }
    }
}