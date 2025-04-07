package com.mthien.event.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NotificationEvent {
    String channel;
    String recepient;
    String teemplateCode;
    Map<String, Object> param;
    String subject;
    String body;
}
