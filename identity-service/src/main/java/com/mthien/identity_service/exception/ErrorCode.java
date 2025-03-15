package com.mthien.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter

public enum ErrorCode {
    UNKNOWN(999, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),

    //AUTH
    INVALID_REFRESH_TOKEN(1001, "Invalid Refresh Token", HttpStatus.UNAUTHORIZED),
    DECODE_ERROR(1001, "Error while decode token", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN(1001, "Invalid Access Token", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(1002, "UNAUTHENTICATED", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_CLIENT(1003, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),

    //USER
    ACCOUNT_NOT_FOUND(1004, "Account Not Found", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1004, "USERNAME_INVALID", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "PASSWORD_INVALID", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1004, "EMAIL_INVALID", HttpStatus.BAD_REQUEST),
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

