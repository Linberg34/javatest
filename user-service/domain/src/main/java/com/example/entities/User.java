package com.example.entities;

import com.example.common.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class User {

    private UUID id;
    private String email;
    private String password;
    private String username;
    private Set<Role> roles;
    private boolean active;
}
