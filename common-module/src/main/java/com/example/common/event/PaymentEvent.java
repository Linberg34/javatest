package com.example.common.event;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentEvent {
    public String paymentId;
    public String userEmail;

}
