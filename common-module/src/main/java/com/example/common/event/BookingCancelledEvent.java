package com.example.common.event;

import java.util.UUID;

public record BookingCancelledEvent(
        UUID bookingId,
        UUID carId,
        UUID userId,
        long   cancelledAt,
        String userEmail
) {}

