package com.carrental.controller;

import com.carrental.dto.BookingResponse;
import com.carrental.dto.CanBookRequest;
import com.carrental.dto.CreateBookingRequest;
import com.carrental.entity.Booking;
import com.carrental.mapper.BookingWebMapper;
import com.carrental.service.interfaces.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingWebMapper bookingWebMapper;

    @PostMapping("/can-book")
    public ResponseEntity<Boolean> canBook(@Valid @RequestBody CanBookRequest rq) {
        boolean available = bookingService.canBook(
                rq.getCarId(),
                rq.getRentFrom(),
                rq.getRentTo()

        );
        return ResponseEntity.ok(available);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(
            @Valid @RequestBody CreateBookingRequest rq,
            Principal principal
    ) {
        UUID userId = UUID.fromString(principal.getName());
        var booking = bookingService.createBooking(
                rq.getCarId(), userId,  rq.getRentFrom(),  rq.getRentTo()
        );
        return bookingWebMapper.toResponse(booking);
    }


}
