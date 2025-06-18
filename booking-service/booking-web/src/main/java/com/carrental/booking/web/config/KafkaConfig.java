package com.carrental.booking.web.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic bookingRequests() {
        return new NewTopic("booking.requests", 1, (short)1);
    }
    @Bean
    public NewTopic bookingCarResponses() {
        return new NewTopic("booking.car-responses", 1, (short)1);
    }
    @Bean
    public NewTopic paymentRequests() {
        return new NewTopic("payment.requests", 1, (short)1);
    }
    @Bean
    public NewTopic paymentResponses() {
        return new NewTopic("payment.responses", 1, (short)1);
    }
    @Bean
    public NewTopic bookingPaymentConfirmed() {
        return new NewTopic("booking.payment-confirmed", 1, (short)1);
    }

}
