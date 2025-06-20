package com.carrental.service.listener;

import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import com.example.common.event.BookingPaymentConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingPaymentConfirmedListener {

    private final CarService carService;

    @KafkaListener(
            topics = "booking.payment-confirmed",
            groupId = "car-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onPaymentConfirmed(BookingPaymentConfirmedEvent evt) {
        log.info(">>> booking.payment-confirmed получено: {}", evt);
        carService.changeStatus(evt.carId(), CarStatus.RENTED);
    }
}
