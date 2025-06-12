package com.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarCreateDto {
    @NotBlank private String make;
    @NotBlank private String model;
    @NotBlank private String plateNumber;
}