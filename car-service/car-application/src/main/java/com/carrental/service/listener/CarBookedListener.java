package com.carrental.service.listener;


import com.carrental.service.interfaces.CarService;
import com.example.common.enums.CarStatus;
import com.example.common.event.CarBookedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarBookedListener {

    private final CarService carService;

    @KafkaListener(
            topics = "car.booked",
            groupId = "car-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void onCarBooked(CarBookedEvent evt) {
        log.info("Received CarBookedEvent: {}", evt);
        carService.changeStatus(evt.carId(), CarStatus.BOOKED);
        log.info("Car {} status set to BOOKED", evt.carId());
    }
}
