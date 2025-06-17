package com.carrental.booking.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@EnableScheduling
@EnableKafka
@SpringBootApplication(
        scanBasePackages = "com.carrental.booking"
)
@EnableJpaRepositories(basePackages = "com.carrental.booking.infrastructure.repository")
@EntityScan(basePackages = "com.carrental.booking.infrastructure.entity")
public class BookingWebApplication {
    public static void main(String[] args) {

        SpringApplication.run(BookingWebApplication.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}
