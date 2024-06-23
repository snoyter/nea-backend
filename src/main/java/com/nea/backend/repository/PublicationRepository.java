package com.nea.backend.repository;

import com.nea.backend.model.Publication;
import com.nea.backend.model.PublicationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    Page<Publication> findAllByTypeOrderByIdDesc(
            Pageable pageable,
            PublicationType publicationType
    );

    Page<Publication> findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCaseOrderByIdDesc(
            Pageable pageable,
            String titleQuery,
            String contentQuery
    );

    Page<Publication> findAllByOrderByIdDesc(Pageable pageable);
}
