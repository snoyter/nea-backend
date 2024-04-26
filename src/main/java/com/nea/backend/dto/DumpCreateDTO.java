package com.nea.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class DumpCreateDTO {
    private String comment;
    private Integer userId;
    private Double longitude;
    private Double latitude;
    private Instant createdAt;
}
