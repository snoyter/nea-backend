package com.nea.backend.dto;

import com.nea.backend.model.User;
import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String email;
    private String role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.role = user.getUserType().getType();
        this.email = user.getLogin();
    }
}
