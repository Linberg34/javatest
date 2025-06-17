package com.carrental.booking.web.security;

import com.carrental.booking.application.service.interfaces.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class BookingSecurity {


    private final BookingService svc;

    public BookingSecurity(BookingService svc) {
        this.svc = svc;
    }

    public boolean isOwner(UUID carId) {

        String owner = svc.findById(carId).getCreatedBy();

        String current = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("null");

        log.debug("isOwner check  ► carId={}  owner={}  current={}", carId, owner, current);

        return owner.equals(current);
    }


}
