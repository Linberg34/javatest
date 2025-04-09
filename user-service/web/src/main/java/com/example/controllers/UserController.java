package com.example.controllers;


import com.application.service.interfaces.AuthService;
import com.application.service.interfaces.UserService;
import com.application.util.TokenPair;
import com.example.dtos.UserRegistrationRequest;
import com.example.entities.User;
import com.example.mappers.UserDtoMapper;
import com.example.repositories.UserRepository;
import com.example.responses.AuthResponse;
import com.example.responses.UserResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService, UserRepository userRepository) {
        this.userService = userService;
        this.authService = authService;
    }

    @PermitAll
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody UserRegistrationRequest request) {
        userService.register(request.getEmail(), request.getPassword());

        TokenPair tokens = authService.login(request.getEmail(), request.getPassword());
        return new AuthResponse(tokens.getAccessToken(), tokens.getRefreshToken());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return UserDtoMapper.toResponse(
                userService.getById(id)
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal User user) {
        return UserDtoMapper.toResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UserRegistrationRequest request) {
        return UserDtoMapper.toResponse(
                userService.update(id, request.getEmail(), request.getPassword())
        );
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAll().stream()
                .map(UserDtoMapper::toResponse)
                .toList();
    }

}
