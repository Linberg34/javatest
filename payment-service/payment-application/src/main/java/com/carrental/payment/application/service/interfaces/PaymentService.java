package com.carrental.payment.application.service.interfaces;

import com.carrental.payment.domain.entity.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    Payment createPayment(UUID bookingId, long amount, String userEmail);
    Payment pay(UUID paymentId);
    Payment cancel(UUID paymentId);
    Payment getById(UUID paymentId);
    List<Payment> getAll(int page, int size);
    List<Payment> getPendingPayments(int page, int size);
}