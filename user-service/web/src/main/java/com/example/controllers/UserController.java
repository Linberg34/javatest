package com.example.controllers;


import com.application.service.interfaces.AuthService;
import com.application.service.interfaces.UserService;
import com.example.common.util.TokenPair;
import com.example.common.dto.UserRegistrationRequest;
import com.example.entities.User;
import com.example.mappers.UserDtoMapper;
import com.example.repositories.UserRepository;
import com.example.common.dto.AuthResponse;
import com.example.common.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @Operation(
            summary = "Зарегистрировать пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно")
            }
    )
    public AuthResponse register(@Valid @RequestBody UserRegistrationRequest request) {
        userService.register(request.getEmail(), request.getUsername(), request.getPassword());

        TokenPair tokens = authService.login(request.getEmail(), request.getPassword());
        return new AuthResponse(tokens.getAccessToken(), tokens.getRefreshToken());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя по ID (для админов)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно")
            }
    )
    public UserResponse getById(@RequestParam(name = "id", required = true) UUID id) {
        return UserDtoMapper.toResponse(
                userService.getById(id)
        );
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    @Operation(
            summary = "Получить данные текущего пользователя",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно")
            }
    )
    public UserResponse getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return UserDtoMapper.toResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные любого пользователя (только для администратора)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public UserResponse updateByAdmin(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UserRegistrationRequest request
    ) {
        return UserDtoMapper.toResponse(
                userService.update(id,
                        request.getEmail(),
                        request.getPassword(),
                        request.getUsername())
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    @Operation(
            summary = "Обновить собственные данные",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public UserResponse updateSelf(@AuthenticationPrincipal User user,
                                   @Valid @RequestBody UserRegistrationRequest request) {
        return UserDtoMapper.toResponse(
                userService.update(user.getId(), request.getEmail(), request.getPassword(), request.getUsername())
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    @Operation(
            summary = "Удалить пользователя (только админ)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно")
            }
    )
    public void deleteByAdmin(
            @RequestParam(name = "id", required = true) UUID id
    ) {
        userService.delete(id);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    @Operation(
            summary = "Удалить свой аккаунт",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно")
            }
    )
    public void deleteSelf(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userService.delete(user.getId());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Получить список пользователей",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно")
            }
    )
    public List<UserResponse> getAllUsers() {
        return userService.getAll().stream()
                .map(UserDtoMapper::toResponse)
                .toList();
    }
}
