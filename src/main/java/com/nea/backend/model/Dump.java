package com.nea.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.Instant;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "public", name = "dump_to_file",
            joinColumns = @JoinColumn(name = "dump_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Picture> pictures;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Dump(
            String comment,
            User user,
            Double longitude,
            Double latitude
    ) {
        this.comment = comment;
        this.user = user;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createdAt = Instant.now();
    }
}
