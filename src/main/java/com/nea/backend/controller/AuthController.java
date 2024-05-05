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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponseModel(jwtToken));
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public void register(@RequestBody UserCreateDTO request) {
        userService.create(request);
    }

}
