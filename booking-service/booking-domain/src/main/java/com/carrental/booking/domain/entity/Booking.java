package com.carrental.booking.domain.entity;


import com.example.common.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
public class Booking {
    private UUID id;
    private UUID carId;
    private UUID userId;

    private BookingStatus status;

    private Instant createdAt;
    private Instant updatedAt;
    private String  createdBy;

    private Instant rentFrom;
    private Instant rentTo;
}
