package com.nea.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "request")
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private Boolean approved;

    @Column(name = "created_at")
    private Instant createdAt;

    public Request(User user) {
        this.user = user;
        this.approved = false;
        this.createdAt = Instant.now();
    }
}
