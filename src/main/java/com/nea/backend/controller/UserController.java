package com.nea.backend.controller;

import com.nea.backend.dto.UserChangePasswordDto;
import com.nea.backend.dto.UserChangeTypeDto;
import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.model.User;
import com.nea.backend.dto.UserDTO;
import com.nea.backend.security.CurrentUser;
import com.nea.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
@Tag(name = "Пользователи")
@Transactional
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CurrentUser currentUser;

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    @Operation(summary = "Получить всех пользователей")
    public Page<User> getUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/current")
    @Operation(summary = "Получить текущего пользователя, если авторизован")
    public UserDTO getUser() {
        return new UserDTO(currentUser.getUser());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/create")
    @Operation(summary = "Создать нового сотрудника")
    public void create(
            @RequestBody UserCreateDTO dto
    ) {
        userService.createEmployeeUser(dto);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update")
    @Operation(summary = "Сменить тип сотрудника")
    public void changeUserType(
            @RequestBody UserChangeTypeDto userChangeTypeDto
    ) {
        userService.changeUserType(userChangeTypeDto);
    }

    @PutMapping("/update/password")
    @Operation(summary = "Обновить пароль")
    public void changePassword(
            @RequestBody UserChangePasswordDto dto
    ) {
        userService.changePassword(dto);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить пользователя")
    public void deleteUser(
            @PathVariable("id") Integer id
    ) {
        userService.deleteById(id);
    }
}
