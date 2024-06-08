package com.nea.backend.model;

import com.nea.backend.dto.PublicationCreateDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "publication")
@NoArgsConstructor
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "type", nullable = false)
    private PublicationType type;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Publication(PublicationCreateDto dto, PublicationType type) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.type = type;
        this.createdAt = Instant.now();
    }
}
