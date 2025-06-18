package com.carrental.payment.web.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic paymentProcessed() {
        return TopicBuilder.name("payment.processed")
                .partitions(1)
                .replicas((short)1)
                .build();
    }

    @Bean
    public NewTopic paymentRequests() {
        return TopicBuilder.name("payment.requests")
                .partitions(1)
                .replicas((short)1)
                .build();
    }
}
