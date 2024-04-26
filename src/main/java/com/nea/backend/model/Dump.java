package com.nea.backend.model;

import com.nea.backend.dto.DumpCreateDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "dump")
@NoArgsConstructor
public class Dump {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Dump(DumpCreateDTO dump, User user) {
        this.comment = dump.getComment();
        this.user = user;
        this.longitude = dump.getLongitude();
        this.latitude = dump.getLatitude();
        this.createdAt = dump.getCreatedAt();
    }
}
