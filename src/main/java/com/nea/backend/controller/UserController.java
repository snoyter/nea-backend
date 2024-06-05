package com.nea.backend.controller;

import com.nea.backend.dto.UserChangePasswordDto;
import com.nea.backend.dto.UserChangeTypeDto;
import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.model.User;
import com.nea.backend.dto.UserDTO;
import com.nea.backend.security.CurrentUser;
import com.nea.backend.service.UserService;
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

    @GetMapping("/all")
    public Page<User> getUsers(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping
    public UserDTO getUser() {
        return new UserDTO(currentUser.getUser());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public void create(
            @RequestBody UserCreateDTO dto
    ) {
        userService.createNewEmployee(dto);
    }

    @PutMapping("/change")
    public void changeUserType(
            @RequestBody UserChangeTypeDto userChangeTypeDto
    ) {
        userService.changeUserType(userChangeTypeDto);
    }

    @PutMapping
    public void changePassword(
            @RequestBody UserChangePasswordDto dto
    ) {
        userService.changePassword(dto);
    }

    @DeleteMapping("{id}")
    public void deleteUser(
            @PathVariable("id") Integer id
    ) {
        userService.delete(id);
    }
}
