package com.nea.backend.dto;

import lombok.Data;

@Data
public class PublicationUpdateDto {
    private Integer id;
    private String title;
    private String content;
    private Integer typeId;
}
