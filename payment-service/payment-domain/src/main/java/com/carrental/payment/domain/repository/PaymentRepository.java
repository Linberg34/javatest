package com.carrental.payment.domain.repository;

import com.carrental.payment.domain.entity.Payment;
import com.example.common.enums.PaymentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
    List<Payment> findAll(int page, int size);
    List<Payment> findByStatus(PaymentStatus status, int page, int size);
    void deleteById(UUID id);
    boolean existsById(UUID id);

}
