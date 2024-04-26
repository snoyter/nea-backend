package com.nea.backend.controller;

import com.nea.backend.dto.DumpCreateDTO;
import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.model.Dump;
import com.nea.backend.model.User;
import com.nea.backend.model.UserType;
import com.nea.backend.repository.UserRepository;
import com.nea.backend.repository.UserTypeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Пользователи")
@Transactional
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    @GetMapping
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @GetMapping("{id}")
    public User getAll(@PathVariable("id") Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Нет такой сущности"));
    }

    @PostMapping
    public User create(@RequestBody UserCreateDTO dto) {
        UserType userType = userTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("нет такого типа пользователей"));
        return userRepository.save(new User(dto, userType));
    }
}
