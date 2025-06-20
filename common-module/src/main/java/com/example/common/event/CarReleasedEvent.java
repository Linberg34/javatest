package com.example.common.event;

import java.util.UUID;

public record CarReleasedEvent(
        UUID bookingId,
        UUID carId,
        long timestamp
) {}