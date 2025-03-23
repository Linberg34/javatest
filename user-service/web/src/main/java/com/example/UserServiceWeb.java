package com.example;

import com.application.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class UserServiceWeb {
    public  static  void main(String[] args){
        SpringApplication.run(UserServiceWeb.class, args);
    }
}
