package com.example;

import com.example.common.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(scanBasePackages = {"com.example", "com.application"})
@EnableConfigurationProperties(JwtProperties.class)
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true)

public class UserServiceWeb {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceWeb.class, args);
    }
}
