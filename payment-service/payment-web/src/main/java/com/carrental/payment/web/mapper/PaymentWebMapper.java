package com.carrental.payment.web.mapper;

import com.carrental.payment.domain.entity.Payment;
import com.carrental.payment.web.dto.responses.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentWebMapper {
    public PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getBookingId(),
                p.getAmount(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getCreatedBy()
        );
    }
}