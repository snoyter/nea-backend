package com.nea.backend.repository;

import com.nea.backend.model.Publication;
import com.nea.backend.model.PublicationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    Page<Publication> findAllByType(Pageable pageable, PublicationType publicationType);

    Page<Publication> findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCase(Pageable pageable, String titleQuery, String contentQuery);
}
