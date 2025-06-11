package com.carrental.mapper;

import com.carrental.entities.Car;
import com.carrental.entities.CarEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T19:41:01+0700",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CarMapperImpl implements CarMapper {

    @Override
    public Car toDomain(CarEntity e) {
        if ( e == null ) {
            return null;
        }

        Car car = new Car();

        car.setId( e.getId() );
        car.setCreatedAt( e.getCreatedAt() );
        car.setUpdatedAt( e.getUpdatedAt() );
        car.setCreatedBy( e.getCreatedBy() );

        return car;
    }

    @Override
    public CarEntity toEntity(Car c) {
        if ( c == null ) {
            return null;
        }

        CarEntity carEntity = new CarEntity();

        carEntity.setId( c.getId() );
        carEntity.setCreatedAt( c.getCreatedAt() );
        carEntity.setUpdatedAt( c.getUpdatedAt() );
        carEntity.setCreatedBy( c.getCreatedBy() );

        return carEntity;
    }
}
