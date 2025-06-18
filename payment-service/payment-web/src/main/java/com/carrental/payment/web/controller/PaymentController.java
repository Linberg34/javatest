package com.carrental.payment.web.controller;


import com.carrental.payment.application.service.interfaces.PaymentService;
import com.carrental.payment.web.dto.requests.CreatePaymentRequest;
import com.carrental.payment.web.dto.responses.PaymentResponse;
import com.carrental.payment.web.mapper.PaymentWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody CreatePaymentRequest rq
    ) {
        var payment = paymentService.createPayment(rq.bookingId(), rq.amount());
        return ResponseEntity
                .status(201)
                .body(mapper.toResponse(payment));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<PaymentResponse> pay(@PathVariable UUID id) {
        var paid = paymentService.pay(id);
        return ResponseEntity.ok(mapper.toResponse(paid));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentResponse> cancel(@PathVariable UUID id) {
        var cancelled = paymentService.cancel(id);
        return ResponseEntity.ok(mapper.toResponse(cancelled));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable UUID id) {
        var p = paymentService.getById(id);
        return ResponseEntity.ok(mapper.toResponse(p));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        var list = paymentService.getAll(pageable.getPageNumber(), pageable.getPageSize());
        var dtos = list.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PaymentResponse>> getPending(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        var list = paymentService.getPendingPayments(pageable.getPageNumber(), pageable.getPageSize());
        var dtos = list.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }
}
