package com.example.mappers;

import com.application.util.TokenPair;
import com.example.responses.AuthResponse;

public class AuthMapper {
    public static AuthResponse toResponse(TokenPair pair) {
        return new AuthResponse(pair.getAccessToken(), pair.getRefreshToken());
    }
}
