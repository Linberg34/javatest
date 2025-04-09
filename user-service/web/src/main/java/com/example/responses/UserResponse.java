package com.example.responses;

import com.example.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
public class UserResponse {
    private UUID id;
    private String email;
    private String username;
    private Set<Role> roles;
    private boolean active;
}
