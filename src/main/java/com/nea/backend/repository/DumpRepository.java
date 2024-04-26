package com.nea.backend.repository;

import com.nea.backend.model.Dump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DumpRepository extends JpaRepository<Dump, Integer> {
}
