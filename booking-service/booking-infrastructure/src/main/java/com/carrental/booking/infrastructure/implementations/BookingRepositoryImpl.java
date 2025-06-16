package com.carrental.booking.infrastructure.implementations;

import com.carrental.booking.domain.entity.Booking;
import com.carrental.booking.infrastructure.mapper.BookingMapper;
import com.carrental.booking.domain.repository.BookingRepository;
import com.carrental.booking.infrastructure.repository.SpringBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final SpringBookingRepository springRepo;
    private final BookingMapper mapper;

    @Override
    public Booking save(Booking booking) {
        var entity = mapper.toEntity(booking);
        var saved = springRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Booking> findById(UUID bookingId) {
        return springRepo.findById(bookingId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Booking> findByUserId(UUID userId) {
        return springRepo.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findByCarId(UUID carId) {
        return springRepo.findByCarId(carId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findOverlapping(UUID carId, Instant from, Instant to) {
        return springRepo.findByCarIdAndRentFromBeforeAndRentToAfter(carId, from, to).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
