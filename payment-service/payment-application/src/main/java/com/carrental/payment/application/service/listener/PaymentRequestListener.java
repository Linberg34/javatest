package com.carrental.payment.application.service.listener;

import com.carrental.payment.application.service.interfaces.PaymentService;
import com.example.common.event.PaymentRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestListener {
    private final PaymentService paymentService;

    @KafkaListener(
            topics = "payment.requests",
            groupId = "payment-service-group"
    )
    public void onPaymentRequested(PaymentRequestedEvent ev) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                ev.userId().toString(), null, AuthorityUtils.NO_AUTHORITIES
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        paymentService.createPayment(ev.bookingId(), ev.amount(), ev.userEmail(), ev.userId());
        SecurityContextHolder.clearContext();
    }
}