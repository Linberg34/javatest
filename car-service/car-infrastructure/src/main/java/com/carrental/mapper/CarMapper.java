package com.carrental.mapper;


import com.carrental.entities.Car;
import com.carrental.entities.CarEntity;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public CarEntity toEntity(Car car) {
        if (car == null) {
            return null;
        }
        CarEntity entity = new CarEntity();
        entity.setId(car.getId());
        entity.setMake(car.getMake());
        entity.setModel(car.getModel());
        entity.setPlateNumber(car.getPlateNumber());
        entity.setStatus(car.getStatus());
        return entity;
    }

    public Car toDomain(CarEntity entity) {
        if (entity == null) {
            return null;
        }
        Car car = new Car();
        car.setId(entity.getId());
        car.setCreatedAt(entity.getCreatedAt());
        car.setUpdatedAt(entity.getUpdatedAt());
        car.setCreatedBy(entity.getCreatedBy());
        car.setMake(entity.getMake());
        car.setModel(entity.getModel());
        car.setPlateNumber(entity.getPlateNumber());
        car.setStatus(entity.getStatus());
        return car;
    }
}