package com.igihecyubuntu.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "picture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String path;

    @Column(name = "alt_text")
    private String altText;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Long doneBy;

    // Many to many relationship with Post through PostPictures
    @ManyToMany(mappedBy = "pictures", fetch = FetchType.LAZY)
    private List<Post> posts;

    @PrePersist
    protected void onCreate() {
        dateTime = LocalDateTime.now();
    }
}