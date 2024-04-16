package com.nea.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "event")
public class Event {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String content;

    @Column(name = "created_at")
    private Instant createdAt;
}
