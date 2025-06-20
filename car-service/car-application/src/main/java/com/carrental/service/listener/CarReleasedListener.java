package com.carrental.service.listener;

import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import com.example.common.event.CarReleasedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarReleasedListener {

    private final CarService carService;

    @KafkaListener(
            topics = "car.released",
            groupId = "car-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void onCarReleased(CarReleasedEvent evt) {
        log.info("Received CarReleasedEvent: {}", evt);
        carService.changeStatus(evt.carId(), CarStatus.FREE);
        log.info("Car {} status set to AVAILABLE", evt.carId());
    }
}
