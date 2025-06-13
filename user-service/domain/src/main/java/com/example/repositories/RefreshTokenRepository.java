package com.example.repositories;

import com.example.entities.User;
import com.example.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
