package com.carrental.payment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentProcessedEvent(
        UUID paymentId,
        UUID bookingId,
        long amount,
        Instant processedAt
) {
}