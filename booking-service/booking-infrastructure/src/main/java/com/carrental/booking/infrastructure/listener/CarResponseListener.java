package com.carrental.booking.infrastructure.listener;

import com.carrental.booking.domain.event.CarReservedEvent;
import com.example.common.event.PaymentRequestedEvent;
import com.carrental.booking.domain.repository.BookingRepository;
import com.example.common.enums.BookingStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CarResponseListener {

    private final BookingRepository repo;
    private final KafkaTemplate<String, Object> kafka;

    public CarResponseListener(BookingRepository repo,
                               KafkaTemplate<String, Object> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }



    @KafkaListener(topics = "booking.car-responses")
    public void onCarResponse(CarReservedEvent ev) {
        repo.findById(ev.bookingId()).ifPresent(b -> {
            if (ev.success()) {
                b.setStatus(BookingStatus.BOOKED);
                repo.save(b);
                kafka.send("payment.requests",
                        new PaymentRequestedEvent(
                                b.getId(),
                                b.getUserId(),
                                b.getAmount(),
                                ev.userEmail()
                        ));
            } else {
                b.setStatus(BookingStatus.FAILED);
                repo.save(b);
            }
        });
    }
}
