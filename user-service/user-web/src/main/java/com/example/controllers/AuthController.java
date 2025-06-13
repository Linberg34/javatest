package com.example.controllers;

import com.application.service.interfaces.AuthService;
import com.example.common.util.TokenPair;
import com.example.common.dto.RefreshTokenRequest;
import com.example.common.dto.UserLoginRequest;
import com.example.common.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Вход пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход"),
                    @ApiResponse(responseCode = "401", description = "Неверные учётные данные")
            }
    )
    public TokenPair login(@RequestBody UserLoginRequest loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @Operation(
            summary = "Рефреш токена"
    )
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = authService.refreshToken(request.getRefreshToken());
        return new AuthResponse(newAccessToken, request.getRefreshToken());
    }
}
