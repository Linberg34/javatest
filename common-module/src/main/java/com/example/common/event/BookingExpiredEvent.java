package com.example.common.event;

import java.util.UUID;

public record BookingExpiredEvent (
    UUID bookingId,
    UUID carId,
    long time
){}
