package com.carrental.notification.service;

import com.example.common.event.BookingEvent;
import com.example.common.event.PaymentEvent;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final SimpMessagingTemplate wsTemplate;

    public NotificationServiceImpl(JavaMailSender mailSender,
                                   SimpMessagingTemplate wsTemplate) {
        this.mailSender = mailSender;
        this.wsTemplate = wsTemplate;
    }

    @Override
    public void notifyNewPayment(PaymentEvent evt) {
        String msg = "Поступил новый платёж #" + evt.getPaymentId();
        wsTemplate.convertAndSend("/topic/notifications", msg);
        sendEmail(evt.getUserEmail(), "Новый платёж на оплату", msg);
    }

    @Override
    public void notifyBookingCancelled(BookingEvent evt) {
        String msg = "Бронирование #" + evt.getBookingId() + " отменено";
        wsTemplate.convertAndSend("/topic/notifications", msg);
        sendEmail(evt.getUserEmail(), "Бронирование отменено", msg);
    }

    @Override
    public void notifyBookingCompleted(BookingEvent evt) {
        String msg = "Аренда оформлена, бронирование #" + evt.getBookingId();
        wsTemplate.convertAndSend("/topic/notifications", msg);
        sendEmail(evt.getUserEmail(), "Аренда автомобиля", msg);
    }

    private void sendEmail(String to, String subj, String body) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(to);
        m.setSubject(subj);
        m.setText(body);
        mailSender.send(m);
    }
}
