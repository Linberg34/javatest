package com.carrental.booking.web.dto;

import com.example.common.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private UUID id;
    private UUID carId;
    private UUID userId;
    private BookingStatus status;
    private Instant rentFrom;
    private Instant rentTo;
    private long amount;
    private UUID paymentId;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
}
