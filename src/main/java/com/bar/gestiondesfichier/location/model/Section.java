package com.bar.gestiondesfichier.location.model;

import com.bar.gestiondesfichier.common.entity.NamedEntity;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sections", indexes = {
    @Index(name = "idx_section_module", columnList = "module_id"),
    @Index(name = "idx_section_name_module", columnList = "name, module_id"),
    @Index(name = "idx_section_code", columnList = "section_code")
})
public class Section extends NamedEntity {

    @Column(name = "section_code", unique = true, length = 20)
    private String sectionCode;

    @Column(name = "section_type", length = 50)
    @Enumerated(EnumType.STRING)
    private SectionType sectionType;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    @JsonBackReference("module-sections")
    private Module module;

    public enum SectionType {
        OFFICE,
        CONFERENCE_ROOM,
        STORAGE,
        LOBBY,
        CORRIDOR,
        RESTROOM,
        KITCHEN,
        SERVER_ROOM,
        PARKING,
        SECURITY,
        RECEPTION,
        ARCHIVE,
        OTHER
    }

    public enum AccessLevel {
        PUBLIC,
        RESTRICTED,
        PRIVATE,
        CONFIDENTIAL,
        TOP_SECRET
    }
}