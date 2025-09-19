package com.bar.gestiondesfichier.location.model;

import com.bar.gestiondesfichier.common.entity.NamedEntity;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "modules", indexes = {
    @Index(name = "idx_module_entity", columnList = "location_entity_id"),
    @Index(name = "idx_module_name_entity", columnList = "name, location_entity_id")
})
public class Module extends NamedEntity {

    @Column(name = "module_code", unique = true, length = 20)
    private String moduleCode;

    @Column(name = "module_type", length = 50)
    @Enumerated(EnumType.STRING)
    private ModuleType moduleType;

    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "area_size")
    private Double areaSize;

    @Column(name = "area_unit", length = 10)
    private String areaUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_entity_id", nullable = false)
    @JsonBackReference("entity-modules")
    private LocationEntity locationEntity;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("module-sections")
    private List<Section> sections;

    public enum ModuleType {
        ADMINISTRATIVE,
        COMMERCIAL,
        RESIDENTIAL,
        INDUSTRIAL,
        AGRICULTURAL,
        RECREATIONAL,
        EDUCATIONAL,
        HEALTHCARE,
        TRANSPORT,
        OTHER
    }
}