package com.carrental.service.implementations;


import com.carrental.entity.Booking;
import com.carrental.repository.BookingRepository;
import com.carrental.service.interfaces.BookingService;
import com.carrental.service.port.CarStatusPort;
import com.carrental.service.port.PaymentPort;
import com.example.common.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repo;
    private final CarStatusPort carPort;
    private final PaymentPort paymentPort;
    private final Clock             clock;

    @Override
    public boolean canBook(UUID carId, Instant from, Instant to) {
        return repo.findOverlapping(carId, from, to).isEmpty();
    }

    @Override
    public Booking createBooking(UUID carId, UUID userId, Instant from, Instant to) {


        var booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setCarId(carId);
        booking.setUserId(userId);
        booking.setRentFrom(from);
        booking.setRentTo(to);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(Instant.now(clock));

        carPort.markBooked(carId);

        return repo.save(booking);
    }

    @Override
    public Booking confirmPayment(UUID bookingId) {
        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));


        boolean ok = paymentPort.charge(bookingId, booking.getUserId(),  0);
        if (!ok) {
            booking.setStatus(BookingStatus.CANCELLED);
            carPort.markFree(booking.getCarId());
        } else {
            booking.setStatus(BookingStatus.RENTED);
            carPort.markRented(booking.getCarId());
        }
        booking.setUpdatedAt(Instant.now(clock));

        return repo.save(booking);
    }

    @Override
    public Booking finishRental(UUID bookingId) {
        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));



        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(Instant.now(clock));

        carPort.markFree(booking.getCarId());
        return repo.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> historyByUser(UUID userId) {
        return repo.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> historyByCar(UUID carId) {
        return repo.findByCarId(carId);
    }
}
