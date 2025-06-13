package com.example.mappers;

import com.example.common.dto.UserRegistrationRequest;
import com.example.entities.User;
import com.example.common.dto.UserResponse;

public class UserDtoMapper {

    public static UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles());
        dto.setActive(user.isActive());
        return dto;
    }

    public static User fromRegistrationRequest(UserRegistrationRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        return user;
    }

}
