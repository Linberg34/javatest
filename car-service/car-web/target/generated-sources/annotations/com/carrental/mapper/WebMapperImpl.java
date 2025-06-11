package com.carrental.mapper;

import com.carrental.dto.CarDTO;
import com.carrental.entities.Car;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T19:41:06+0700",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class WebMapperImpl implements WebMapper {

    @Override
    public CarDTO toDto(Car domain) {
        if ( domain == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();

        return carDTO;
    }

    @Override
    public Car toDomain(CarDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Car car = new Car();

        return car;
    }
}
