package com.example.common.event;


import java.util.UUID;

public record PaymentRequestedEvent(
        UUID bookingId,
        UUID userId,
        long amount
) {
}
