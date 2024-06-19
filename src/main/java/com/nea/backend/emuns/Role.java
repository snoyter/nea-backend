package com.nea.backend.emuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    USER(2),
    ADMIN(1);

    private final Integer id;
}
