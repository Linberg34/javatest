package com.carrental.booking.domain.event;

import java.util.UUID;

public record CarReservedEvent(
        UUID bookingId,
        boolean success
) {
}
