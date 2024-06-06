package com.nea.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "file")
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String type;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Picture(String name, String content, String type) {
        this.name = name;
        this.content = content;
        this.type = type;
        this.createdAt = Instant.now();
    }
}
