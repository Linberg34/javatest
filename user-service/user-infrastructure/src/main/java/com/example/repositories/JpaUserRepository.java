package com.example.repositories;

import com.example.entities.User;
import com.example.entities.UserEntity;
import com.example.mappers.UserEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserJpaRepository jpa;

    public JpaUserRepository(SpringDataUserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public User findById(UUID id) {
        return jpa.findById(id)
                .map(UserEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        return UserEntityMapper.toDomain(jpa.save(entity));
    }

    @Override
    public void delete(UUID id) {
    }

    @Override
    public User findByEmail(String email) {
        return jpa.findByEmail(email)
                .map(UserEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return jpa.findAll()
                .stream()
                .map(UserEntityMapper::toDomain)
                .toList();
    }


}
