package com.application.service.interfaces;

import com.example.entities.User;

import java.util.UUID;

public interface TokenService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String getEmailFromToken(String token);

    boolean validateToken(String token);

    UUID getUserIdFromToken(String token);
}