package com.carrental.mapper;


import com.carrental.dto.CarCreateDto;
import com.carrental.dto.CarDTO;
import com.carrental.dto.CarUpdateDto;
import com.carrental.entities.Car;

public final class CarDtoMapper {

    private CarDtoMapper() {
    }


    public static CarDTO toDto(Car car) {
        if (car == null) return null;

        return CarDTO.builder()
                .id(car.getId())
                .createdAt(car.getCreatedAt())
                .updatedAt(car.getUpdatedAt())
                .createdBy(car.getCreatedBy())
                .make(car.getMake())
                .model(car.getModel())
                .plateNumber(car.getPlateNumber())
                .status(car.getStatus())
                .build();
    }

    public static Car fromCreate(CarCreateDto dto) {
        if (dto == null) return null;

        Car car = new Car();
        car.setMake(dto.getMake());
        car.setModel(dto.getModel());
        car.setPlateNumber(dto.getPlateNumber());
        return car;
    }

    public static Car fromUpdate(CarUpdateDto dto) {
        if (dto == null) return null;
        Car car = new Car();
        car.setMake(dto.getMake());
        car.setModel(dto.getModel());
        car.setPlateNumber(dto.getPlateNumber());
        return car;
    }
}