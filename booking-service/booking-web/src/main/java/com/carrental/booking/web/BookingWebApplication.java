package com.carrental.booking.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = "com.carrental.booking"
)
@EnableFeignClients(
        basePackages = "com.carrental.booking.infrastructure.adapter"
)
public class BookingWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingWebApplication.class, args);
    }
}
