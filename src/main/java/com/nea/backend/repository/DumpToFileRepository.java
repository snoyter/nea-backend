package com.nea.backend.repository;

import com.nea.backend.model.DumpToFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DumpToFileRepository extends JpaRepository<DumpToFiles, DumpToFiles.Key> {
}
