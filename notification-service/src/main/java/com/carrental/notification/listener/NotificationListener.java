package com.carrental.notification.listener;

import com.carrental.notification.service.NotificationService;
import com.example.common.event.BookingCancelledEvent;
import com.example.common.event.BookingCompletedEvent;
import com.example.common.event.PaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final NotificationService svc;

    public NotificationListener(NotificationService svc) {
        this.svc = svc;
    }

    @KafkaListener(topics = "payment.new", containerFactory = "kafkaListenerContainerFactory")
    public void onNewPayment(PaymentEvent evt) {

        svc.notifyNewPayment(evt);
    }

    @KafkaListener(topics = "booking.cancelled", containerFactory = "kafkaListenerContainerFactory")
    public void onBookingCancelled(BookingCancelledEvent evt) {
        svc.notifyBookingCancelled(evt);
    }

    @KafkaListener(topics = "booking.completed", containerFactory = "kafkaListenerContainerFactory")
    public void onBookingCompleted(BookingCompletedEvent evt) {
        svc.notifyBookingCompleted(evt);
    }
}
