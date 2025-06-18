package com.carrental.payment.web.dto.responses;

import com.example.common.enums.PaymentStatus;

import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID bookingId,
        long amount,
        PaymentStatus status,
        Instant createdAt,
        Instant updatedAt,
        String createdBy
) {}