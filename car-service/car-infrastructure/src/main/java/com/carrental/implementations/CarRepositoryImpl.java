package com.carrental.implementations;

import com.carrental.entities.Car;
import com.carrental.entities.CarEntity;
import com.carrental.mapper.CarMapper;
import com.carrental.repository.CarRepository;
import com.carrental.repository.SpringCarRepository;
import com.example.common.enums.CarStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class CarRepositoryImpl implements CarRepository {
    private final SpringCarRepository repo;
    private final CarMapper mapper;
    @PersistenceContext
    private EntityManager entityManager;

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
    public void deleteById(UUID id) {
        repo.deleteById(id);
    }

    @Override
    public Car save(Car car) {
        CarEntity entity = mapper.toEntity(car);
        CarEntity saved = entityManager.merge(entity);
        return mapper.toDomain(saved);
    }

    public Car update(UUID id, Car car) {
        CarEntity existing = entityManager.find(CarEntity.class, id);
        if (existing == null) {
            throw new RuntimeException("Car not found: " + id);
        }
        existing.setMake(car.getMake());
        existing.setModel(car.getModel());
        existing.setPlateNumber(car.getPlateNumber());
        CarEntity updated = entityManager.merge(existing);
        return mapper.toDomain(updated);
    }
}