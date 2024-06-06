package com.nea.backend.repository;

import com.nea.backend.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<Picture, Integer> {
    Optional<Picture> findByContent(String content);
}
