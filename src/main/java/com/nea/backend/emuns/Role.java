package com.nea.backend.emuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    USER(1),
    ADMIN(2);

    private final Integer id;
}
