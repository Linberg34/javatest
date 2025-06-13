package com.example.mappers;

import com.example.entities.User;
import com.example.entities.UserEntity;

public class UserEntityMapper {

    public static User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRoles(entity.getRoles());
        user.setActive(entity.isActive());
        return user;
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRoles(user.getRoles());
        entity.setActive(user.isActive());
        return entity;
    }
}


