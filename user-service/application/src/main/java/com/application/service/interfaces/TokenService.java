package com.application.service.interfaces;

import com.example.entities.User;

public interface TokenService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String getEmailFromToken(String token);

    boolean validateToken(String token);
}
