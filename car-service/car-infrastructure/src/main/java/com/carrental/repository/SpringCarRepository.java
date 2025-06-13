package com.carrental.repository;

import com.carrental.entities.CarEntity;
import com.example.common.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringCarRepository extends JpaRepository<CarEntity, UUID> {
    List<CarEntity> findByStatus(CarStatus status);
}
