package com.example.common.event;

import java.util.UUID;

public record BookingPaymentConfirmedEvent(
        UUID bookingId,
        UUID carId,
        UUID userId
) {}