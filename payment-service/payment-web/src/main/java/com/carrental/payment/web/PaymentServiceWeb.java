package com.carrental.payment.web;


import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Clock;
import java.util.Optional;

@EnableScheduling
@EnableKafka
@EnableJpaRepositories(basePackages = "com.carrental.payment.infrastructure.repository")
@EntityScan(basePackages     = "com.carrental.payment.infrastructure.entity")
@SpringBootApplication(
        scanBasePackages = "com.carrental.payment"
)
public class PaymentServiceWeb {
    public static void main(String[] args) {

        SpringApplication.run(PaymentServiceWeb.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        Logger log = LoggerFactory.getLogger(PaymentServiceWeb.class);
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String userId = auth.getName();
                log.debug("AuditorAware: Returning userId={}", userId);
                return Optional.of(userId);
            }
            log.debug("AuditorAware: Returning default 'system'");
            return Optional.of("system");
        };
    }

}
