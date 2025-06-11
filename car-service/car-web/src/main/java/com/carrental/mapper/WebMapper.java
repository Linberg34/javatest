package com.carrental.mapper;

import com.carrental.dto.CarDTO;
import com.carrental.entities.Car;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface WebMapper {
    CarDTO toDto(Car domain);
    Car toDomain(CarDTO dto);

}
