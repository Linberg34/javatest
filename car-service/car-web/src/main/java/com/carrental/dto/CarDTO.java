package com.carrental.dto;

import com.example.common.enums.CarStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CarDTO {
    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String make;
    private String model;
    private String plateNumber;
    private CarStatus status;
}
