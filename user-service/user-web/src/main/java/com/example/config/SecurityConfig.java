package com.example.config;

import com.application.security.JwtAuthFilter;
import com.application.service.interfaces.TokenService;
import com.example.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   TokenService tokenService,
                                                   UserRepository userRepository) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/users/register",
                                "/register",
                                "/swagger-ui/**",
                                "/cars/**",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(
                new JwtAuthFilter(tokenService, userRepository),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
