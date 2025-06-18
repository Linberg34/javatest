package com.carrental.payment.web.controller;


import com.carrental.payment.application.service.interfaces.PaymentService;
import com.carrental.payment.web.dto.requests.CreatePaymentRequest;
import com.carrental.payment.web.dto.responses.PaymentResponse;
import com.carrental.payment.web.mapper.PaymentWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentWebMapper mapper;

    @PostMapping
    @Operation(summary = "Создать платеж")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody CreatePaymentRequest rq
    ) {
        var payment = paymentService.createPayment(rq.bookingId(), rq.amount());
        return ResponseEntity
                .status(201)
                .body(mapper.toResponse(payment));
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "Оплатить")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> pay(@PathVariable("id") UUID id) {
        var paid = paymentService.pay(id);
        return ResponseEntity.ok(mapper.toResponse(paid));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Отменить платеж")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> cancel(@PathVariable("id") UUID id) {
        var cancelled = paymentService.cancel(id);
        return ResponseEntity.ok(mapper.toResponse(cancelled));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить платеж по id")
    @PreAuthorize("hasRole('ADMIN') or @paymentSecurity.isOwner(#id)")
    public ResponseEntity<PaymentResponse> getById(@PathVariable("id") UUID id) {
        var p = paymentService.getById(id);
        return ResponseEntity.ok(mapper.toResponse(p));
    }

    @GetMapping
    @Operation(summary = "Получить все платежи(админ)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        var list = paymentService.getAll(pageable.getPageNumber(), pageable.getPageSize());
        var dtos = list.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/pending")
    @Operation(summary = "Получить все платежи в процессе обработки(админ)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getPending(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        var list = paymentService.getPendingPayments(pageable.getPageNumber(), pageable.getPageSize());
        var dtos = list.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }
}
