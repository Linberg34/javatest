package com.carrental.controller;

import com.carrental.dto.*;
import com.carrental.mapper.CarDtoMapper;
import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Cars", description = "API для управления автомобилями")
@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }


    @GetMapping
    @Operation(summary = "Получить все автомобили")
    public List<CarDTO> listAll() {
        return carService.listAll()
                .stream()
                .map(CarDtoMapper::toDto)
                .toList();
    }

    @GetMapping("/available")
    @Operation(summary = "Получить доступные автомобили")
    public List<CarDTO> listAvailable() {
        return carService.listAvailable()
                .stream()
                .map(CarDtoMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить автомобиль по ID")
    public CarDTO getById( @PathVariable("id") UUID id) {
        return CarDtoMapper.toDto(carService.getById(id));
    }


    @PostMapping
    @Operation(summary = "Создать новый автомобиль")
    public ResponseEntity<CarDTO> create(@Valid @RequestBody CarCreateDto dto) {
        System.out.println("DTO Values: make=" + dto.getMake() + ", model=" + dto.getModel() + ", plateNumber=" + dto.getPlateNumber());
        var created = carService.create(CarDtoMapper.fromCreate(dto));
        return ResponseEntity
                .created(URI.create("/cars/" + created.getId()))
                .body(CarDtoMapper.toDto(created));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные автомобиля")
    public CarDTO update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CarUpdateDto dto) {
        return CarDtoMapper.toDto(carService.update(id, CarDtoMapper.fromUpdate(dto)));
    }


    @PostMapping("/{id}/status")
    @Operation(summary = "Изменить статус автомобиля")
    public CarDTO changeStatus( @PathVariable("id") UUID id,
                               @RequestParam CarStatus status) {
        return CarDtoMapper.toDto(carService.changeStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить автомобиль")
    public void delete(@PathVariable("id") UUID id) {
        carService.delete(id);
    }
}
