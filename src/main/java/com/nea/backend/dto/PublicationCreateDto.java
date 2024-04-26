package com.nea.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicationCreateDto {
    private String title;
    private String content;
    private Integer typeId;
}
