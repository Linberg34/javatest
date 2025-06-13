package com.example.implementations;

import com.example.entities.RefreshToken;
import com.example.entities.RefreshTokenEntity;
import com.example.entities.User;
import com.example.entities.UserEntity;
import com.example.mappers.RefreshTokenEntityMapper;
import com.example.repositories.RefreshTokenJpaRepository;
import com.example.repositories.RefreshTokenRepository;
import com.example.repositories.SpringDataUserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpa;
    private final SpringDataUserJpaRepository userJpa;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository jpa, SpringDataUserJpaRepository userJpa) {
        this.jpa = jpa;
        this.userJpa = userJpa;
    }

    @Override
    public RefreshToken save(RefreshToken token) {
        UserEntity userEntity = userJpa.findById(token.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        RefreshTokenEntity entity = RefreshTokenEntityMapper.toEntity(token, userEntity);
        return RefreshTokenEntityMapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpa.findByToken(token)
                .map(RefreshTokenEntityMapper::toDomain);
    }

    @Override
    public void deleteByUser(User user) {
        UserEntity userEntity = userJpa.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        jpa.deleteByUser(userEntity);
    }
}
