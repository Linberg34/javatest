package com.carrental.booking.infrastructure.listener;


import com.carrental.booking.application.service.interfaces.BookingService;
import com.example.common.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCreatedListener {

    private final BookingService bookingService;

    @KafkaListener(
            topics    = "payment.new",
            groupId   = "booking-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onPaymentCreated(PaymentEvent evt) {
        log.info("Получили событие создания оплаты: {}", evt);
        bookingService.linkPaymentToBooking(evt.getBookingId(), evt.getPaymentId());
    }
}
