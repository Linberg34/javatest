package com.application.service.implementations;

import com.application.exceptions.UserNotFoundException;
import com.application.service.interfaces.UserService;
import com.example.entities.User;
import com.example.enums.Role;
import com.example.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(Role.Client));
        user.setActive(true);

        return userRepository.save(user);
    }

    public User getById(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    public void delete(UUID id) {
        if (userRepository.findById(id) == null) {
            throw new RuntimeException("User not found");
        }
        User user = userRepository.findById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll().stream()
                .filter(User::isActive)
                .toList();
    }


    public User update(UUID id, String email, String password) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return userRepository.save(user);
    }

}
