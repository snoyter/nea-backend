package com.nea.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserChangeTypeDto {
    private Integer id;
    private Integer userTypeId;
}
