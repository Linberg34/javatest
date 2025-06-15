package com.carrental.adapter.payment;


import com.carrental.service.port.PaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentPort {
    private final PaymentServiceClient client;

    @Override
    public boolean charge(UUID bookingId, UUID userId, long amount) {
        var req = new PaymentServiceClient.ChargeRequest(bookingId, userId, amount);
        var resp = client.charge(req);
        return resp.success();
    }
}
