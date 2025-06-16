package com.carrental.booking.domain.repository;

import com.carrental.booking.domain.entity.Booking;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID bookingId);
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByCarId(UUID carId);
    List<Booking> findOverlapping(UUID carId, Instant from, Instant to);
}
