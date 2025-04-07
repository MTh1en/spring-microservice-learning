package com.mthien.file_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter

public enum ErrorCode {
    UNKNOWN(999, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),

    //AUTH
    UNAUTHENTICATED(1002, "UNAUTHENTICATED", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_CLIENT(1003, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),

    FILE_NOT_FOUND(1004, "File Not Found", HttpStatus.NOT_FOUND),
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

