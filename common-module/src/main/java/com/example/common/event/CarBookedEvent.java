package com.example.common.event;


import java.util.UUID;

public record CarBookedEvent(
        UUID bookingId,
        UUID carId,
        UUID userId,
        long timestamp
) {}