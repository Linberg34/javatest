package com.carrental.notification.service;


import com.example.common.event.BookingCancelledEvent;
import com.example.common.event.BookingCompletedEvent;
import com.example.common.event.PaymentEvent;

public interface NotificationService {
    void notifyNewPayment(PaymentEvent evt);
    void notifyBookingCancelled(BookingCancelledEvent evt);
    void notifyBookingCompleted(BookingCompletedEvent evt);
}