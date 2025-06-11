package com.carrental.entities;

import com.example.common.enums.CarStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class Car  {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;

    private String make;
    private String model;
    private String plateNumber;
    private CarStatus status;
}
