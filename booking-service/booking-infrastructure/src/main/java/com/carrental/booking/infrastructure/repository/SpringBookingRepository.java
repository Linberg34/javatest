package com.carrental.booking.infrastructure.repository;

import com.carrental.booking.infrastructure.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SpringBookingRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findByUserId(UUID userId);

    List<BookingEntity> findByCarId(UUID carId);

    List<BookingEntity> findByCarIdAndRentFromBeforeAndRentToAfter(
            UUID carId,
            Instant from,
            Instant to
    );
}
