package com.carrental.booking.infrastructure.adapter.payment;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "payment-service", url = "${services.payment.url}")
public interface PaymentServiceClient {
    @PostMapping("/api/payments/charge")
    ChargeResponse charge(@RequestBody ChargeRequest request);

    record ChargeRequest(UUID bookingId, UUID userId, long amount) {}
    record ChargeResponse(boolean success) {}
}
