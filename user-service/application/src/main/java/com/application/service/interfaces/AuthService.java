package com.application.service.interfaces;

import com.application.util.TokenPair;
import com.example.entities.User;


public interface AuthService {
    TokenPair login(String email, String password);
    String refreshToken(String refreshToken);
    boolean validateToken(String token);
    User getUserFromToken(String token);
}
