package com.carrental.service.implementations;

import com.carrental.entities.Car;
import com.carrental.repository.CarRepository;
import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository repo;

    public CarServiceImpl(CarRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Car> listAll() {
        return repo.findAll();
    }

    @Override
    public List<Car> listAvailable() {
        return repo.findByStatus(CarStatus.FREE);
    }

    @Override
    public Car getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found: " + id));
    }

    @Override
    public Car create(Car car) {
        car.setStatus(CarStatus.FREE);
        return repo.save(car);
    }

    @Override
    public Car update(UUID id, Car car) {
        Car existing = getById(id);
        existing.setMake(car.getMake());
        existing.setModel(car.getModel());
        existing.setPlateNumber(car.getPlateNumber());
        return repo.save(existing);
    }

    @Override
    public Car changeStatus(UUID id, CarStatus newStatus) {
        Car c = getById(id);
        c.setStatus(newStatus);
        return repo.save(c);
    }
    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }

}
