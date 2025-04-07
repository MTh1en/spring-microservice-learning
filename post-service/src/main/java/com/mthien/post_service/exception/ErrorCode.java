package com.mthien.post_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter

public enum ErrorCode {
    UNKNOWN(999, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),

    UNAUTHENTICATED(1002, "UNAUTHENTICATED", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_CLIENT(1003, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}

