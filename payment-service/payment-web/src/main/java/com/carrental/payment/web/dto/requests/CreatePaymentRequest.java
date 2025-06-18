package com.carrental.payment.web.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePaymentRequest(
        @NotNull UUID bookingId,
        @Min(1) long amount
) {}