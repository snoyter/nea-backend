package com.nea.backend.controller;

import com.nea.backend.dto.PublicationCreateDto;
import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.model.*;
import com.nea.backend.repository.PublicationRepository;
import com.nea.backend.repository.PublicationTypeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publication")
@Tag(name = "Публикации")
@Transactional
@RequiredArgsConstructor
public class PublicationController {
    private final PublicationRepository publicationRepository;
    private final PublicationTypeRepository publicationTypeRepository;

    @GetMapping
    public Page<Publication> getAll(Pageable pageable) {
        return publicationRepository.findAll(pageable);
    }

    @GetMapping("{id}")
    public Publication getAll(@PathVariable("id") Integer id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Нет такой сущности"));
    }

    @PostMapping
    public Publication create(@RequestBody PublicationCreateDto dto) {
        PublicationType type = publicationTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("нет такого типа контента"));
        return publicationRepository.save(new Publication(dto, type));
    }
}
