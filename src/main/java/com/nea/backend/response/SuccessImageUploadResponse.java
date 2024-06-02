package com.nea.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SuccessImageUploadResponse {
    private List<Integer> uploadedImagesIds;
}
