package com.nea.backend.model;

import com.nea.backend.dto.UserCreateDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(schema = "public", name = "usr")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "type", nullable = false)
    private UserType type;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public User(UserCreateDTO dto, UserType userType) {
        this.name = dto.getName();
        this.login = dto.getLogin();
        this.password = dto.getPassword();
        this.type = userType;
        this.createdAt = Instant.now();
    }
}
