package com.mthien.identity_service.exception;

import com.mthien.identity_service.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error(e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.UNKNOWN;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(e.getMessage())
                        .data(null)
                        .build());
    }
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode()).body(ApiResponse.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .data(null)
                .build());
    }
}
