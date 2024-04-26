package com.nea.backend.controller;

import com.nea.backend.dto.DumpCreateDTO;
import com.nea.backend.model.Dump;
import com.nea.backend.model.User;
import com.nea.backend.repository.DumpRepository;
import com.nea.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dump")
@Tag(name = "Свалки")
@Transactional
@RequiredArgsConstructor
public class DumpController {
    private final DumpRepository dumpRepository;
    private final UserRepository userRepository;

    @GetMapping
    public Page<Dump> getAll(Pageable pageable) {
        return dumpRepository.findAll(pageable);
    }

    @GetMapping("{id}")
    public Dump getAll(@PathVariable("id") Integer id) {
        return dumpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Нет такой сущности"));
    }

    @PostMapping
    public Dump create(@RequestBody DumpCreateDTO dump) {
        User user = userRepository.findById(dump.getUserId())
                .orElseThrow(() -> new RuntimeException("нет такого пользователя"));
        return dumpRepository.save(new Dump(dump, user));
    }
}
