package com.carrental.mapper;


import com.carrental.entities.Car;
import com.carrental.entities.CarEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CarMapper {
    Car toDomain(CarEntity e);
    CarEntity toEntity(Car c);
}
