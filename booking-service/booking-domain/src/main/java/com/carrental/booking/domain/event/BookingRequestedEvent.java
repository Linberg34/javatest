package com.carrental.booking.domain.event;

import java.util.UUID;

public record BookingRequestedEvent(
        UUID bookingId,
        UUID carId,
        UUID userId,
        long timestamp
) {
}
