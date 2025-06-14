package com.carrental.service.interfaces;

import com.carrental.entity.Booking;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface BookingService {
    boolean canBook(UUID carId, Instant from, Instant to);

    Booking createBooking(UUID carId, UUID userId, Instant from, Instant to);

    Booking confirmPayment(UUID bookingId);

    Booking finishRental(UUID bookingId);

    List<Booking> historyByUser(UUID userId);

    List<Booking> historyByCar(UUID carId);
}
