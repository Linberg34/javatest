package com.example.common.dto;

import com.example.common.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class UserResponse {
    private String email;
    private String username;
    private Set<Role> roles;
    private boolean active;
}
