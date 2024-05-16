package com.bookshop.notificationservice.serivce;

import com.bookshop.notificationservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        sendEmail(orderPlacedEvent.getOrderNumber(), orderPlacedEvent.getUserEmail());
        log.info("Received notification for Order - {}", orderPlacedEvent.getOrderNumber());
    }

    public void sendEmail(String orderId, String receiver){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(receiver);
        message.setSubject("Latyshev-Shop Delivery");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("New Order ") .append(orderId).append(" - was placed successfully");

        message.setText(stringBuilder.toString());
        try {
            mailSender.send(message);
        } catch (MailException e){
            e.printStackTrace();
        }
    }
}
