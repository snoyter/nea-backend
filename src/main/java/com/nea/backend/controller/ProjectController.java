package com.nea.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dump")
@Tag(name = "Проекты")
@Transactional
@RequiredArgsConstructor
public class ProjectController {
}
