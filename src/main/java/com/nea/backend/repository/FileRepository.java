package com.nea.backend.repository;

import com.nea.backend.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Integer> {
    Optional<File> findByContent(String content);
}
