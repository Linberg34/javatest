package com.example.repositories;
import com.example.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User findById(UUID id);
    User save(User user);
    User findByEmail(String email);
    void delete(UUID id);
    List<User> findAll();

}
