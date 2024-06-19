package com.nea.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailDTO {
    private String name;
    private String email;
    private String message;
}
