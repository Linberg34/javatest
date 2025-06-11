package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(
        exclude = {
                ServletWebServerFactoryAutoConfiguration.class,
                DispatcherServletAutoConfiguration.class,
                ErrorMvcAutoConfiguration.class,
                WebMvcAutoConfiguration.class
        }
)
public class ApiGateway {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiGateway.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
