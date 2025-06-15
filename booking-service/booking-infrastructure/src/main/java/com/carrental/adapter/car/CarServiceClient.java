package com.carrental.adapter.car;

import com.example.common.enums.CarStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "car-service", url = "${services.car.url}")
public interface CarServiceClient {
    @GetMapping("/api/cars/{id}/status")
    CarStatus getStatus(@PathVariable("id") UUID carId);

    @PostMapping("/api/cars/{id}/status")
    void updateStatus(@PathVariable("id") UUID carId,
                      @RequestParam("status") CarStatus status);
}
