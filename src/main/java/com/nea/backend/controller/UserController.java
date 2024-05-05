package com.nea.backend.controller;

import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.model.User;
import com.nea.backend.model.UserType;
import com.nea.backend.repository.UserRepository;
import com.nea.backend.repository.UserTypeRepository;
import com.nea.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final UserService userService;

    @GetMapping("me")
    public User getCurrentUser(HttpServletRequest request) {
        Integer id = (Integer)request.getSession().getAttribute("id");
        return userService.getOneById(id);
    }

    @GetMapping
    public Page<User> getAll(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping("{id}")
    public User getAll(@PathVariable("id") Integer id) {
        return userService.getOneById(id);
    }

    @PostMapping
    public User create(@RequestBody UserCreateDTO dto) {
        return userService.create(dto);
    }
}
