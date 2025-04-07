package com.mthien.notification_service.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mthien.notification_service.payload.request.SendEmailRequest;
import com.mthien.notification_service.payload.response.ApiResponse;
import com.mthien.notification_service.payload.response.EmailResponse;
import com.mthien.notification_service.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;

    @PostMapping("/email/send")
    ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest request){
        return ApiResponse.<EmailResponse>builder()
                .message("Email sent successfully")
                .data(emailService.sendEmail(request))
                .build();
    }
}
