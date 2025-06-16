package com.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
@ComponentScan(
        basePackages = "com.carrental",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = FeignClient.class
        )
)
@EnableFeignClients(basePackages = "com.carrental.adapter")
public class BookingWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingWebApplication.class, args);
    }
}
