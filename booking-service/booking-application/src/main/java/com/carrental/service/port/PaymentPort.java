package com.carrental.service.port;

import java.util.UUID;

public interface PaymentPort {

    boolean charge(UUID bookingId, UUID userId, long amount);
}
