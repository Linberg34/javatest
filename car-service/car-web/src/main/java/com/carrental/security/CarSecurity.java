package com.carrental.security;

import com.carrental.service.interfaces.CarService;
import lombok.extern.slf4j.Slf4j;           //  Lombok @Slf4j
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j                                       // <-- добавьте
@Component
public class CarSecurity {

    private final CarService svc;

    public CarSecurity(CarService svc) {
        this.svc = svc;
    }

    public boolean isOwner(UUID carId) {
        // кто записан в БД
        String owner = svc.getById(carId).getCreatedBy();

        // кто пришёл в заголовке X-User-Id и попал в SecurityContext
        String current = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("null");

        log.debug("isOwner check  ► carId={}  owner={}  current={}", carId, owner, current);

        return owner.equals(current);
    }
}
