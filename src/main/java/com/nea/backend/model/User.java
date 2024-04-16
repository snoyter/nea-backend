package com.nea.backend.model;

import com.nea.backend.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "usr")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    private String login;

    private String password;

    private UserType type;

    @Column(name = "created_at")
    private Instant createdAt;

    private String name;
}
