package com.nea.backend.controller;

import com.nea.backend.exception.ApiError;
import com.nea.backend.exception.ApiError.UserAlreadyConnected;
import com.nea.backend.model.Request;
import com.nea.backend.model.User;
import com.nea.backend.repository.RequestRepository;
import com.nea.backend.security.CurrentUser;
import com.nea.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/request")
@Tag(name = "Сотрудничество")
@Transactional
@RequiredArgsConstructor
public class RequestController {
    private final CurrentUser currentUser;
    private final RequestRepository repository;
    private final UserService userService;

    @PostMapping("/connect")
    @Operation(summary = "Создать новый запрос на сотрудничество")
    public void connect() {
        User user = currentUser.getUser();
        try {
            repository.save(new Request(user));
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyConnected();
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все запросы на сотрудничество")
    public List<Request> getAllRequests() {
        return repository.findAllByApprovedFalse();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/approve")
    @Operation(summary = "Подключить человека к проекту")
    public void changeUserType(
            @RequestParam Integer id
    ) {
        User user = userService.giveAdminToUserById(id);
        Request request = repository.findByUser(user);
        request.setApproved(true);
        repository.save(request);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/disapprove")
    @Operation(summary = "Подключить человека к проекту")
    public void disapproveRequest(
            @RequestParam Integer id
    ) {
        User user = userService.findById(id);
        Request request = repository.findByUser(user);
        repository.delete(request);
    }
}
