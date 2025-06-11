package com.carrental.service.interfaces;

import com.carrental.entities.Car;
import com.example.common.enums.CarStatus;

import java.util.List;
import java.util.UUID;

public interface CarService {
    List<Car> listAll();
    List<Car> listAvailable();
    Car getById(UUID id);
    Car create(Car car);
    Car update(Car car);
    Car changeStatus(UUID id, CarStatus newStatus);
}