package com.mthien.notification_service.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.mthien.event.dto.NotificationEvent;
import com.mthien.notification_service.payload.request.Recipient;
import com.mthien.notification_service.payload.request.SendEmailRequest;
import com.mthien.notification_service.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent message) {
        log.info("Received message: " + message);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder()
                        .email(message.getRecepient())
                        .build())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }
}
