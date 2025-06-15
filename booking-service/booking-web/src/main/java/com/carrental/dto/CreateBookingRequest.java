package com.carrental.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    @NotNull
    UUID carId;
    @NotNull
    @Future(message = "rentFrom must be in the future")
    Instant rentFrom;
    @NotNull
    @Future(message = "rentTo must be in the future")
    Instant rentTo;
}
