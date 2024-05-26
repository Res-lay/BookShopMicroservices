package com.bookshop.notificationservice.serivce;

import com.bookshop.notificationservice.event.OrderPlacedEvent;
import com.bookshop.notificationservice.model.OrderLineItems;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationService {
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        sendEmail(orderPlacedEvent.getUsername(), orderPlacedEvent.getUserEmail(), orderPlacedEvent.getOrder());
    }

    public void sendEmail(String username, String receiver, List<OrderLineItems> items){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(receiver);
        message.setSubject("Latyshev-Shop Delivery");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello, ").append(username).append(" \nHere is your order:\n");
        int i = 1;
        stringBuilder.append("------------------------------\n");
        for (OrderLineItems item : items) {
            stringBuilder.append(i).append(". ").append(item.getSkuCode()).append(" - ")
                    .append(item.getQuantity()).append(" items;\n");
            i ++;
        }
        message.setText(stringBuilder.toString());
        try {
            mailSender.send(message);
        } catch (MailException e){
            e.printStackTrace();
        }
    }
}
