package com.carrental;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages="com.carrental.adapter")
public class BookingWebApplication {
    public static void main(String[] args) {

    }
}