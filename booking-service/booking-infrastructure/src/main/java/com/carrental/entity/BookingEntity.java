package com.carrental.entity;

import com.example.common.entity.BaseAuditableEntity;
import com.example.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity extends BaseAuditableEntity {

    @Column(nullable = false)
    private UUID carId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false)
    private Instant rentFrom;

    @Column(nullable = false)
    private Instant rentTo;
}
