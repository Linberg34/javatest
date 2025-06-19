package com.carrental.payment.application.service.listener;

import com.carrental.payment.application.service.interfaces.PaymentService;
import com.example.common.event.PaymentRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestListener {
    private final PaymentService paymentService;

    @KafkaListener(
            topics = "payment.requests",
            groupId = "payment-service-group"
    )
    public void onPaymentRequested(PaymentRequestedEvent ev) {
        log.info("+++ получено PaymentRequestedEvent: {}", ev);
        paymentService.createPayment(ev.bookingId(), ev.amount());
    }
}