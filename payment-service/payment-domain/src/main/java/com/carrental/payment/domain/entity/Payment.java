package com.carrental.payment.domain.entity;

import com.example.common.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
public class Payment {
    private UUID id;
    private UUID bookingId;
    private long amount;
    private PaymentStatus status;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;

}
