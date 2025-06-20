package com.application.service.implementations;

import com.application.security.JwtProvider;
import com.application.service.interfaces.AuthService;
import com.example.common.security.JwtProperties;
import com.example.common.util.TokenPair;
import com.example.entities.RefreshTokenEntity;
import com.example.entities.UserEntity;
import com.example.entities.User;
import com.example.mappers.UserEntityMapper;
import com.example.repositories.RefreshTokenJpaRepository;
import com.example.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenJpaRepository refreshTokenRepo;
    private final JwtProperties jwtProperties;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder,
                           RefreshTokenJpaRepository refreshTokenRepo,
                           JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepo = refreshTokenRepo;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public TokenPair login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken  = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        UserEntity userEntity = UserEntityMapper.toEntity(user);

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(UUID.randomUUID());
        entity.setUser(userEntity);
        entity.setToken(refreshToken);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setExpiresAt(LocalDateTime.now()
                .plusSeconds(parseSeconds(jwtProperties.getRefreshTokenExpiration())));
        refreshTokenRepo.save(entity);

        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public String refreshToken(String oldRefreshToken) {
        if (!jwtProvider.validateToken(oldRefreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        RefreshTokenEntity entity = refreshTokenRepo.findByToken(oldRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (entity.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }
        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepo.delete(entity);
            throw new RuntimeException("Refresh token expired");
        }

        UserEntity userEntity = entity.getUser();
        User user = UserEntityMapper.toDomain(userEntity);

        String newAccess  = jwtProvider.generateAccessToken(user);
        String newRefresh = jwtProvider.generateRefreshToken(user);

        entity.setToken(newRefresh);
        entity.setExpiresAt(LocalDateTime.now()
                .plusSeconds(parseSeconds(jwtProperties.getRefreshTokenExpiration())));
        entity.setRevoked(false);
        refreshTokenRepo.save(entity);

        return newAccess;
    }

    private long parseSeconds(String duration) {
        if (duration.endsWith("m")) return Long.parseLong(duration.replace("m", "")) * 60;
        if (duration.endsWith("h")) return Long.parseLong(duration.replace("h", "")) * 3600;
        if (duration.endsWith("d")) return Long.parseLong(duration.replace("d", "")) * 86400;
        return Long.parseLong(duration);
    }
}
