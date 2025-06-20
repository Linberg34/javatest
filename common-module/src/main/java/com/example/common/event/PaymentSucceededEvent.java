package com.example.common.event;

import java.util.UUID;

public record PaymentSucceededEvent(
        UUID bookingId,
        boolean success,
        UUID paymentId,
        String userEmail
) {
}
