package com.carrental.booking.infrastructure.listener;


import com.carrental.booking.application.service.interfaces.BookingService;
import com.example.common.event.PaymentSucceededEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentResponseListener {

    private final BookingService bookingService;

    @KafkaListener(
            topics   = "payment.responses",
            groupId  = "booking-service-group"
    )
    public void onPaymentResponse(PaymentSucceededEvent ev) {
        log.info(">>> onPaymentRequested: {}", ev);
        if (ev.success()) {
            bookingService.confirmPayment(ev.bookingId(), ev.paymentId());
        } else {
            bookingService.rejectPayment(ev.bookingId(), ev.userEmail());
        }
    }
}
