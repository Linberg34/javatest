package com.carrental.controller;


import com.carrental.dto.CarDTO;
import com.carrental.mapper.WebMapper;
import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final WebMapper mapper;

    public CarController(CarService carService, WebMapper mapper) {
        this.carService = carService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<CarDTO> listAll() {
        return carService.listAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/available")
    public List<CarDTO> listAvailable() {
        return carService.listAvailable().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarDTO getById(@PathVariable UUID id) {
        return mapper.toDto(carService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CarDTO> create(@RequestBody CarDTO dto) {
        var created = carService.create(mapper.toDomain(dto));
        var result  = mapper.toDto(created);
        return ResponseEntity
                .created(URI.create("/api/cars/" + result.getId()))
                .body(result);
    }

    @PutMapping("/{id}")
    public CarDTO update(@PathVariable UUID id, @RequestBody CarDTO dto) {
        dto.setId(id);
        return mapper.toDto(carService.update(mapper.toDomain(dto)));
    }

    @PostMapping("/{id}/status")
    public CarDTO changeStatus(@PathVariable UUID id,
                               @RequestParam("status")CarStatus status) {
        return mapper.toDto(carService.changeStatus(id, status));
    }

}
