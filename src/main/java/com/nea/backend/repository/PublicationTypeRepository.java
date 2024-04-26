package com.nea.backend.repository;

import com.nea.backend.model.PublicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationTypeRepository extends JpaRepository<PublicationType, Integer> {
}
