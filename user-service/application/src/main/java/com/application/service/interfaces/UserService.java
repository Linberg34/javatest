package com.application.service.interfaces;

import com.example.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User register(String email, String password);

    User getById(UUID id);

    void delete(UUID id);

    User update(UUID id, String email, String password);

    List<User> getAll();

}
