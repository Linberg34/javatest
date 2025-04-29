package com.application.service.interfaces;

import com.example.common.util.TokenPair;


public interface AuthService {
    TokenPair login(String email, String password);

    String refreshToken(String refreshToken);
}
