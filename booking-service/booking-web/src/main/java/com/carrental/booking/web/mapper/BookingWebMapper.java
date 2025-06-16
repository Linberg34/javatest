package com.carrental.booking.web.mapper;


import com.carrental.booking.web.dto.BookingResponse;
import com.carrental.booking.domain.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingWebMapper {


    public BookingResponse toResponse(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getCarId(),
                b.getUserId(),
                b.getStatus(),
                b.getRentFrom(),
                b.getRentTo(),
                b.getCreatedAt(),
                b.getUpdatedAt(),
                b.getCreatedBy()
        );
    }
}
