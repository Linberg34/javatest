package com.carrental.booking.infrastructure.listener;

import com.carrental.booking.domain.event.PaymentSucceededEvent;
import com.carrental.booking.domain.repository.BookingRepository;
import com.example.common.enums.BookingStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentResponseListener {

    private final BookingRepository repo;

    public PaymentResponseListener(BookingRepository repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "payment.responses")
    public void onPaymentResponse(PaymentSucceededEvent ev) {
        repo.findById(ev.bookingId())
                .ifPresent(b -> {
                    if (ev.success()) {
                        b.setStatus(BookingStatus.PAID);
                        b.setPaymentId(ev.paymentId());
                    } else {
                        b.setStatus(BookingStatus.FAILED);
                    }
                    repo.save(b);
                });
    }
}
