package com.carrental.payment.infrastructure.mapper;

import com.carrental.payment.domain.entity.Payment;
import com.carrental.payment.infrastructure.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(payment.getId());
        entity.setBookingId(payment.getBookingId());
        entity.setAmount(payment.getAmount());
        entity.setStatus(payment.getStatus());
        entity.setCreatedAt(payment.getCreatedAt());
        entity.setUpdatedAt(payment.getUpdatedAt());
        entity.setCreatedBy(payment.getCreatedBy());
        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        Payment payment = new Payment();
        payment.setId(entity.getId());
        payment.setBookingId(entity.getBookingId());
        payment.setAmount(entity.getAmount());
        payment.setStatus(entity.getStatus());
        payment.setCreatedAt(entity.getCreatedAt());
        payment.setUpdatedAt(entity.getUpdatedAt());
        payment.setCreatedBy(entity.getCreatedBy());
        return payment;
    }
}
