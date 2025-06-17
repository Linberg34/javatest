package com.carrental.booking.web.controller;

import com.carrental.booking.application.service.interfaces.BookingService;
import com.carrental.booking.web.dto.BookingResponse;
import com.carrental.booking.web.dto.CanBookRequest;
import com.carrental.booking.web.dto.CreateBookingRequest;
import com.carrental.booking.web.mapper.BookingWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody CreateBookingRequest rq,
            Principal principal
    ) {
        UUID userId = UUID.fromString(principal.getName());
        var booking = bookingService.createBooking(
                rq.getCarId(), userId, rq.getRentFrom(), rq.getRentTo()
        );
        return ResponseEntity.accepted()
                .body(bookingWebMapper.toResponse(booking));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> getById(@PathVariable UUID id) {
        var booking = bookingService.findById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookingWebMapper.toResponse(booking));
    }


    @PostMapping("/{id}/finish")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> finish(@PathVariable UUID id) {
        var booking = bookingService.finishRental(id);
        return ResponseEntity.ok(bookingWebMapper.toResponse(booking));
    }


    @GetMapping("/history/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingResponse>> historyMe(Principal p) {
        UUID userId = UUID.fromString(p.getName());
        var list = bookingService.historyByUser(userId)
                .stream().map(bookingWebMapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/history/car/{carId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingResponse>> historyByCar(@PathVariable UUID carId) {
        var list = bookingService.historyByCar(carId)
                .stream().map(bookingWebMapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }
}

