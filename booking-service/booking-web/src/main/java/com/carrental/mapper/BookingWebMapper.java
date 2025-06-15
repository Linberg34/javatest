package com.carrental.mapper;


import com.carrental.dto.BookingResponse;
import com.carrental.entity.Booking;
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
                b.getUpdatedAt()
        );
    }
}
