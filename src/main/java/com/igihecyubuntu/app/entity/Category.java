package com.igihecyubuntu.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "done_by", nullable = false)
    private Long doneBy;

    // Many to many relationship with Post through PostCategory
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Post> posts;
}