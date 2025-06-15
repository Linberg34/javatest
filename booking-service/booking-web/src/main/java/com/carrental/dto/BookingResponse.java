package com.carrental.dto;

import com.example.common.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class BookingResponse {
    public UUID id;
    public UUID carId;
    public UUID userId;
    public BookingStatus status;
    public Instant rentFrom;
    public Instant rentTo;
    public Instant createdAt;
    public Instant updatedAt;
    public String createdBy;
}
