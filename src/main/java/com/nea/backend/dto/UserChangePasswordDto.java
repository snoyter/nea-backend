package com.nea.backend.dto;

import lombok.Data;

@Data
public class UserChangePasswordDto {
    private final String oldPassword;
    private final String newPassword;
}
