package com.nea.backend.model.publication;

import com.nea.backend.model.publication.PublicationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "publication")
public class Publication {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String content;

    @Enumerated
    private PublicationType type;

    @Column(name = "created_at")
    private Instant createdAt;
}
