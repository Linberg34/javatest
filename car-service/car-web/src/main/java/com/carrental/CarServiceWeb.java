package com.carrental;

import com.example.common.util.UserPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@SpringBootApplication(scanBasePackages = {"com.carrental", "com.carrental.implementations", "com.carrental.infrastructure"})
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableMethodSecurity(prePostEnabled = true)
public class CarServiceWeb {
    public static void main(String[] args) {
        SpringApplication.run(CarServiceWeb.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    Object principal = auth.getPrincipal();
                    if (principal instanceof UserPrincipal up) {
                        return up.getEmail();
                    }
                    return auth.getName();
                })
                .or(() -> Optional.of("system"));
    }
}