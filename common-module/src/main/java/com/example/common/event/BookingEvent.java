package com.example.common.event;


import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class BookingEvent {
    public String bookingId;
    public String userEmail;
}
