package com.application.service.interfaces;

import com.example.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User register(String email, String username, String password);

    User getById(UUID id);

    void delete(UUID id);

    User update(UUID id, String email, String password, String username);

    List<User> getAll();

}
