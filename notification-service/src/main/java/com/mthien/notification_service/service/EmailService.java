package com.mthien.notification_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mthien.notification_service.exception.AppException;
import com.mthien.notification_service.exception.ErrorCode;
import com.mthien.notification_service.payload.request.EmailRequest;
import com.mthien.notification_service.payload.request.SendEmailRequest;
import com.mthien.notification_service.payload.request.Sender;
import com.mthien.notification_service.payload.response.EmailResponse;
import com.mthien.notification_service.repository.httpclient.EmailClient;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    String apiKey = "YOUR_API_KEY_HERE";

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
            .sender(Sender.builder()
                    .name("MThien")
                    .email("mt12122003@gmail.com")
                    .build())
            .to(List.of(request.getTo()))
            .subject(request.getSubject())
            .htmlContent(request.getHtmlContent())
            .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
