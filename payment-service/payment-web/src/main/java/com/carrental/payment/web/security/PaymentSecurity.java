package com.carrental.payment.web.security;

import com.carrental.payment.application.service.interfaces.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentSecurity {
    private final PaymentService svc;

    public PaymentSecurity(PaymentService svc) {
        this.svc = svc;
    }

    public boolean isOwner(UUID carId) {

        String owner = svc.getById(carId).getCreatedBy();

        String current = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("null");

        log.debug("isOwner check  ► carId={}  owner={}  current={}", carId, owner, current);

        return owner.equals(current);
    }
}
