package com.carrental.service.listener;


import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import com.example.common.event.BookingExpiredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingExpiredListener {

    private final CarService carService;

    @KafkaListener(
            topics = "booking.expired",
            groupId = "car-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void onBookingExpired(BookingExpiredEvent evt) {
        log.info("Received BookingExpiredEvent: {}", evt);
        carService.changeStatus(evt.carId(), CarStatus.FREE);
        log.info("Car {} status set to AVAILABLE (FREE)", evt.carId());
    }
}
