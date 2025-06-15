package com.carrental.adapter.car;

import com.carrental.service.port.CarStatusPort;
import com.example.common.enums.CarStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class CarStatusAdapter implements CarStatusPort {
    private final CarServiceClient client;

    @Override
    public CarStatus getStatus(UUID carId) {
        return client.getStatus(carId);
    }

    @Override
    public void markBooked(UUID carId) {
        client.updateStatus(carId, CarStatus.RENTED);
    }

    @Override
    public void markRented(UUID carId) {
        client.updateStatus(carId, CarStatus.RENTED);
    }

    @Override
    public void markFree(UUID carId) {
        client.updateStatus(carId, CarStatus.FREE);
    }
}
