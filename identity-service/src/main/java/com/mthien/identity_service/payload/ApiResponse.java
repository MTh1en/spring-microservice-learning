package com.mthien.identity_service.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    @Builder.Default
    private int code = 1000;
    private String message;
    private T data;
}
