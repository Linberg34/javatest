package com.carrental.booking.application.service.implementations;

import com.carrental.booking.application.service.interfaces.BookingService;
import com.carrental.booking.domain.entity.Booking;
import com.carrental.booking.domain.event.BookingRequestedEvent;
import com.carrental.booking.domain.repository.BookingRepository;
import com.example.common.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, Object> kafka;
    private final Clock clock;

    @Override
    public boolean canBook(UUID carId, Instant from, Instant to) {
        return repo.findOverlapping(carId, from, to).isEmpty();
    }

    @Override
    public Booking createBooking(UUID carId, UUID userId, Instant from, Instant to) {
        if (!canBook(carId, from, to)) {
            throw new IllegalStateException("Car is already booked for the given period");
        }

        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setCarId(carId);
        booking.setUserId(userId);
        booking.setRentFrom(from);
        booking.setRentTo(to);
        booking.setAmount(calculateAmount(from, to));
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(Instant.now(clock));
        booking.setCreatedBy(String.valueOf(userId));

        repo.save(booking);

        kafka.send("booking.requests",
                new BookingRequestedEvent(
                        booking.getId(),
                        booking.getCarId(),
                        booking.getUserId(),
                        Instant.now(clock).toEpochMilli()
                ));

        return booking;
    }

    @Override
    public Booking confirmPayment(UUID bookingId) {
        //TODO: обработка платежа
        throw new UnsupportedOperationException(
                "Payment confirmation is handled asynchronously via Kafka");
    }

    @Override
    public Booking finishRental(UUID bookingId) {
        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(Instant.now(clock));
        repo.save(booking);

        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking findById(UUID bookingId) {
        return repo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
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

    private long calculateAmount(Instant from, Instant to) {
        long hours = java.time.Duration.between(from, to).toHours();
        return hours * 1000;
    }
}
