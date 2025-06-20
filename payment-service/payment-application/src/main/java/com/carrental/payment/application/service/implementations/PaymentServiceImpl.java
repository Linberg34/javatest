package com.carrental.payment.application.service.implementations;

import com.carrental.payment.application.service.interfaces.PaymentService;
import com.carrental.payment.domain.entity.Payment;
import com.carrental.payment.domain.event.PaymentProcessedEvent;
import com.carrental.payment.domain.repository.PaymentRepository;
import com.example.common.enums.PaymentStatus;
import com.example.common.event.PaymentEvent;
import com.example.common.event.PaymentSucceededEvent;
import com.example.common.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Payment createPayment(UUID bookingId, long amount, String userEmail, UUID userId) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }

            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setAmount(amount);
            payment.setStatus(PaymentStatus.NEW_PAYMENT);
            payment.setCreatedAt(Instant.now());
            payment.setUpdatedAt(Instant.now());
            payment.setCreatedBy(String.valueOf(userId));

            Payment savedPayment = paymentRepository.save(payment);
            log.info("Created new payment with ID: {}", savedPayment.getId());

            PaymentEvent evt = new PaymentEvent(savedPayment.getId(), savedPayment.getBookingId(),userId, userEmail);
            kafkaTemplate.send("payment.new", evt);
            return savedPayment;
        } catch (Exception e) {
            log.error("Ошибка при создании платежа для bookingId: {}, amount: {}", bookingId, amount, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Payment pay(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        if (!payment.getStatus().equals(PaymentStatus.NEW_PAYMENT)) {
            throw new IllegalStateException("Payment can only be processed if it is new");
        }

        log.info("Processing payment with ID: {} for amount: {}", paymentId, payment.getAmount());
        try {
            Thread.sleep(1000);
            payment.setStatus(PaymentStatus.PAID);
            payment.setUpdatedAt(Instant.now());
            Payment updatedPayment = paymentRepository.save(payment);

            PaymentProcessedEvent event = new PaymentProcessedEvent(
                    paymentId,
                    payment.getBookingId(),
                    payment.getAmount(),
                    Instant.now()
            );
            kafkaTemplate.send("payment.processed", event);
            log.info("Payment processed and event sent for payment ID: {}", paymentId);

            return updatedPayment;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Payment processing interrupted", e);
        }
    }

    @Override
    @Transactional
    public Payment cancel(UUID paymentId) {
        String email = SecurityUtils.currentUserEmail();

        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Cannot cancel a paid payment");
        }
        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setUpdatedAt(Instant.now());
        var updated = paymentRepository.save(payment);

        log.info("Updated payment object: {}", updated);

        kafkaTemplate.send("payment.responses",
                new PaymentSucceededEvent(
                        updated.getBookingId(),
                        false,
                        updated.getId(),
                        email

                )
        );

        log.info("Cancelled payment with ID: {}, sent failure event to payment.responses", paymentId);
        return updated;
    }


    @Override
    @Transactional(readOnly = true)
    public Payment getById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAll(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }
        return paymentRepository.findAll(page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPendingPayments(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }
        return paymentRepository.findByStatus(PaymentStatus.NEW_PAYMENT, page, size);
    }
}