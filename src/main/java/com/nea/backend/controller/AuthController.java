package com.nea.backend.controller;

import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.dto.UserLoginDTO;
import com.nea.backend.security.JwtResponseModel;
import com.nea.backend.security.JwtUserDetailsService;
import com.nea.backend.security.TokenManager;
import com.nea.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * Модуль авторизации
 * */
@CrossOrigin
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизация")
public class AuthController {
    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final TokenManager tokenManager;

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<JwtResponseModel> login(@RequestBody UserLoginDTO request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Set-Cookie", "accessToken=" + jwtToken +"; HttpOnly; Secure;");
        return new ResponseEntity<>(new JwtResponseModel(jwtToken), headers, HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public void register(@RequestBody UserCreateDTO request) {
        userService.create(request);
    }
}
