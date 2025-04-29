package com.example.mappers;

import com.example.common.util.TokenPair;
import com.example.common.dto.AuthResponse;

public class AuthMapper {
    public static AuthResponse toResponse(TokenPair pair) {
        return new AuthResponse(pair.getAccessToken(), pair.getRefreshToken());
    }
}
