package com.nea.backend.controller;

import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.model.User;
import com.nea.backend.dto.UserDTO;
import com.nea.backend.security.CurrentUser;
import com.nea.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Пользователи")
@Transactional
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CurrentUser currentUser;

    @GetMapping
    @Operation(summary = "Получить информацию о пользователе")
    public UserDTO getUser() {
        return new UserDTO(currentUser.getUser());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public User create(@RequestBody UserCreateDTO dto) {
        return userService.create(dto);
    }
}
