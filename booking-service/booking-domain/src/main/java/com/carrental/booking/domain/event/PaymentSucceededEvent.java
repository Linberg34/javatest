package com.carrental.booking.domain.event;

import java.util.UUID;

public record PaymentSucceededEvent(
        UUID bookingId,
        boolean success,
        UUID paymentId
) {
}
