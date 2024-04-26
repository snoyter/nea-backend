package com.nea.backend.repository;

import com.nea.backend.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
}
