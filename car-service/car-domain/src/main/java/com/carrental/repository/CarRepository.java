package com.carrental.repository;

import com.carrental.entities.Car;
import com.example.common.enums.CarStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {

    List<Car> findAll();
    List<Car> findByStatus(CarStatus status);
    Optional<Car> findById(UUID id);
    void deleteById(UUID id);
    Car save (Car car);
    Car update(UUID id, Car car);
}
