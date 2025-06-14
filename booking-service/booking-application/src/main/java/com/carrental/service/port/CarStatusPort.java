package com.carrental.service.port;


import com.example.common.enums.CarStatus;
import java.util.UUID;

public interface CarStatusPort {
    CarStatus getStatus(UUID carId);
    void markBooked(UUID carId);
    void markRented(UUID carId);
    void markFree(UUID carId);
}
