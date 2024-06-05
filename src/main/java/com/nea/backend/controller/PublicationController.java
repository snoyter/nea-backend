package com.nea.backend.controller;

import com.nea.backend.dto.PublicationCreateDto;
import com.nea.backend.dto.PublicationUpdateDto;
import com.nea.backend.model.Publication;
import com.nea.backend.model.PublicationType;
import com.nea.backend.repository.PublicationRepository;
import com.nea.backend.repository.PublicationTypeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/publication")
@Tag(name = "Публикации")
@Transactional
@RequiredArgsConstructor
public class PublicationController {
    private final PublicationRepository publicationRepository;
    private final PublicationTypeRepository publicationTypeRepository;

    @GetMapping
    public Page<Publication> getAll(
            Pageable pageable,
            @RequestParam(value = "type",required = false) Integer publicationType
    ) {
        if (publicationType != null) {
            Optional<PublicationType> type = publicationTypeRepository.findById(publicationType);
            if (type.isPresent()) {
                return publicationRepository.findAllByType(pageable, type.get());
            }
        }
        return publicationRepository.findAll(pageable);
    }

    @PutMapping
    public void update(
            @RequestBody PublicationUpdateDto dto
    ) {
        Publication publicationFromDB = publicationRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Нет такой публикации!"));
        publicationFromDB.setContent(dto.getContent());
        publicationFromDB.setTitle(dto.getTitle());
        PublicationType publicationType = publicationTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("Нет такого типа данных!"));
        publicationFromDB.setType(publicationType);
        publicationRepository.save(publicationFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable("id") Integer id
    ) {
        publicationRepository.deleteById(id);
    }

    @GetMapping("/search")
    public Page<Publication> search(
            Pageable pageable,
            @RequestParam("search") String searchQuery
    ) {
        return publicationRepository.findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCase(
                pageable,
                searchQuery,
                searchQuery
        );
    }

    @GetMapping("{id}")
    public Publication getAll(@PathVariable("id") Integer id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Нет такой сущности"));
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public Publication create(@RequestBody PublicationCreateDto dto) {
        PublicationType type = publicationTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("нет такого типа контента"));
        return publicationRepository.save(new Publication(dto, type));
    }
}
