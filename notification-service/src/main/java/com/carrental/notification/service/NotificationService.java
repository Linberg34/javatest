package com.carrental.notification.service;


import com.example.common.event.BookingEvent;
import com.example.common.event.PaymentEvent;

public interface NotificationService {
    void notifyNewPayment(PaymentEvent evt);
    void notifyBookingCancelled(BookingEvent evt);
    void notifyBookingCompleted(BookingEvent evt);
}