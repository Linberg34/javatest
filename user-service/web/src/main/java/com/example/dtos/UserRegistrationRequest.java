package com.example.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 2, max = 64)
    private String username;

    @NotBlank
    @Size(min = 6, max = 32)
    private String password;
}
