package com.carrental.service.listener;

import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import com.example.common.event.BookingCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCancelledListener {

    private final CarService carService;

    @KafkaListener(
            topics = "booking.cancelled",
            groupId = "car-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void onBookingCancelled(BookingCancelledEvent evt) {
        log.info("Received BookingCancelledEvent: {}", evt);
        carService.changeStatus(evt.carId(), CarStatus.FREE);
        log.info("Car {} status set to AVAILABLE (FREE)", evt.carId());
    }
}
