package com.carrental.implementations;

import com.carrental.entities.Car;
import com.carrental.entities.CarEntity;
import com.carrental.mapper.CarMapper;
import com.carrental.repository.CarRepository;
import com.carrental.repository.SpringCarRepository;
import com.example.common.enums.CarStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class CarRepositoryImpl implements CarRepository {

    private final SpringCarRepository repo;
    private final CarMapper mapper;

    public CarRepositoryImpl(SpringCarRepository repo, CarMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<Car> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Car> findByStatus(CarStatus status) {
        return List.of();
    }


    @Override
    public Optional<Car> findById(UUID id) {
        return repo.findById(id).map(mapper::toDomain);
    }


    @Override
    public Car save(Car car) {
        CarEntity e = mapper.toEntity(car);
        CarEntity saved = repo.save(e);
        return mapper.toDomain(saved);
    }
}
