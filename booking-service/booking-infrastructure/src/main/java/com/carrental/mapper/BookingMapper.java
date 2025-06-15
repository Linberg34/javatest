package com.carrental.mapper;


import com.carrental.entity.Booking;
import com.carrental.entity.BookingEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingEntity toEntity(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingEntity entity = new BookingEntity();
        entity.setId(booking.getId());
        entity.setCarId(booking.getCarId());
        entity.setUserId(booking.getUserId());
        entity.setStatus(booking.getStatus());
        entity.setRentFrom(booking.getRentFrom());
        entity.setRentTo(booking.getRentTo());
        return entity;
    }

    public Booking toDomain(BookingEntity entity) {
        if (entity == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(entity.getId());
        booking.setCarId(entity.getCarId());
        booking.setUserId(entity.getUserId());
        booking.setStatus(entity.getStatus());
        booking.setRentFrom(entity.getRentFrom());
        booking.setRentTo(entity.getRentTo());
        booking.setCreatedAt(entity.getCreatedAt());
        booking.setUpdatedAt(entity.getUpdatedAt());
        booking.setCreatedBy(entity.getCreatedBy());
        return booking;
    }
}
