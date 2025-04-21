package com.example.mappers;

import com.example.entities.RefreshToken;
import com.example.entities.RefreshTokenEntity;
import com.example.entities.UserEntity;

public class RefreshTokenEntityMapper {

    public static RefreshToken toDomain(RefreshTokenEntity e) {
        RefreshToken token = new RefreshToken();
        token.setId(e.getId());
        token.setToken(e.getToken());
        token.setCreatedAt(e.getCreatedAt());
        token.setExpiresAt(e.getExpiresAt());
        token.setRevoked(e.isRevoked());
        return token;
    }

    public static RefreshTokenEntity toEntity(RefreshToken token, UserEntity userEntity) {
        RefreshTokenEntity e = new RefreshTokenEntity();
        e.setId(token.getId());
        e.setToken(token.getToken());
        e.setCreatedAt(token.getCreatedAt());
        e.setExpiresAt(token.getExpiresAt());
        e.setRevoked(token.isRevoked());
        e.setUser(userEntity);
        return e;
    }
}
