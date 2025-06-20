package com.carrental.booking.web.controller;

import com.carrental.booking.application.service.interfaces.BookingService;
import com.carrental.booking.web.dto.BookingResponse;
import com.carrental.booking.web.dto.CanBookRequest;
import com.carrental.booking.web.dto.CanBookResponse;
import com.carrental.booking.web.dto.CreateBookingRequest;
import com.carrental.booking.web.mapper.BookingWebMapper;
import com.example.common.util.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Operation(summary = "Проверка на возможность аренды")
    public ResponseEntity<CanBookResponse> canBook(@Valid @RequestBody CanBookRequest rq) {
        boolean available = bookingService.canBook(
                rq.getCarId(),
                rq.getRentFrom(),
                rq.getRentTo()
        );

        if (available) {
            return ResponseEntity
                    .ok(new CanBookResponse(true, "Car is available"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new CanBookResponse(false, "Car is already booked for the given period"));
        }
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Создать аренду")
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody CreateBookingRequest rq,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        if (!bookingService.canBook(rq.getCarId(), rq.getRentFrom(), rq.getRentTo())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        UUID userId = UUID.fromString(user.getId());
        var booking = bookingService.createBooking(
                rq.getCarId(), userId, rq.getRentFrom(), rq.getRentTo()
        );
        return ResponseEntity
                .accepted()
                .body(bookingWebMapper.toResponse(booking));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookingSecurity.isOwner(#id)")
    @Operation(summary = "Получить аренду по id")
    public ResponseEntity<BookingResponse> getById(
            @PathVariable("id") UUID id
    ) {
        var booking = bookingService.findById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookingWebMapper.toResponse(booking));
    }


    @PostMapping("/{id}/finish")
    @PreAuthorize("hasRole('ADMIN') or @bookingSecurity.isOwner(#id)")
    @Operation(summary = "Завершить аренду")
    public ResponseEntity<BookingResponse> finish(@PathVariable("id") UUID id) {
        try {
            var booking = bookingService.finishRental(id);
            return ResponseEntity.ok( bookingWebMapper.toResponse(booking) );
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().startsWith("Booking not found")) {
                return ResponseEntity.notFound().build();
            }
            throw ex;
        } catch (IllegalStateException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
    }


    @GetMapping("/history/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "История аренды текущего пользователя")
    public ResponseEntity<List<BookingResponse>> historyMe(Principal p) {
        UUID userId = UUID.fromString(p.getName());
        var list = bookingService.historyByUser(userId)
                .stream().map(bookingWebMapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/history/car/{carId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "История аренды по машине")
    public ResponseEntity<List<BookingResponse>> historyByCar(
            @PathVariable("carId") UUID carId
    ) {
        var list = bookingService.historyByCar(carId)
                .stream()
                .map(bookingWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

}

