package com.example.common.event;

import java.util.UUID;

public record BookingCompletedEvent(
        UUID bookingId,
        UUID carId,
        UUID userId,
        long   completedAt,
        String userEmail
) {}