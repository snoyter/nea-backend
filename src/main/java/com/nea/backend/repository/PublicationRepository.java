package com.nea.backend.repository;

import com.nea.backend.model.Publication;
import com.nea.backend.model.PublicationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    Page<Publication> findAllByType(Pageable pageable, PublicationType publicationType);
}
