package com.nea.backend.model;

import com.nea.backend.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "dump")
public class Dump {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @ManyToOne
    private User user;

    private Integer longitude;

    private Integer latitude;

    @Column(name = "created_at")
    private Instant createdAt;
}
