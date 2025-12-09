package com.storebackend.model.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
public enum Errors {

    NO_CODE(0, NOT_IMPLEMENTED, "error.code.0"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "error.code.300"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "error.code.301"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "error.code.302"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "error.code.303"),
    BAD_CREDENTIALS(304, FORBIDDEN, "error.code.304"),
    PAGE_NOT_FOUND(404, NOT_FOUND, "error.code.404"),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.code.500");

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    Errors(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
