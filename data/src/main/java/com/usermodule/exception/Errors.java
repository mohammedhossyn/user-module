package com.usermodule.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
public enum Errors {
    INCORRECT_CURRENT_PASSWORD(400, BAD_REQUEST, "error.incorrect.current.password"),
    NEW_PASSWORD_DOES_NOT_MATCH(400, BAD_REQUEST, "error.new.password.does.not.match"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(400, BAD_REQUEST, "error.method.argument.not.valid.exception"),
    ILLEGAL_ARGUMENT_EXCEPTION(400, BAD_REQUEST, "error.illegal.argument.exception"),
    CONSTRAINT_VIOLATION_EXCEPTION(400, BAD_REQUEST, "error.constraint.violation.exception"),
    ACTIVATION_TOKEN_EXCEPTION(401, UNAUTHORIZED, "error.activation.token.exception"),
    BAD_CREDENTIALS(401, UNAUTHORIZED, "error.bad.credentials"),
    AUTHENTICATION_EXCEPTION(401, UNAUTHORIZED, "error.authentication.exception"),
    INSUFFICIENT_AUTHENTICATION_EXCEPTION(401, UNAUTHORIZED, "error.insufficient.authentication.exception"),
    ACCESS_DENIED_EXCEPTION(401, UNAUTHORIZED, "error.access.denied.exception"),
    ACCOUNT_DISABLED(403, FORBIDDEN, "error.account.disabled"),
    OPERATION_NOT_PERMITTED_EXCEPTION(403, FORBIDDEN, "error.operation.not.permitted.exception"),
    PAGE_NOT_FOUND(404, NOT_FOUND, "error.page.not.found"),
    NO_HANDLER_FOUND_EXCEPTION(404, NOT_FOUND, "error.no.handler.found.exception"),
    SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION(409, CONFLICT, "error.sql.integrity.constraint.violation.exception"),
    DATA_INTEGRITY_VIOLATION_EXCEPTION(409, CONFLICT, "error.data.integrity.violation.exception"),
    ALREADY_EXISTS_EXCEPTION(409, CONFLICT, "error.already.exists.exception"),
    BUSINESS_EXCEPTION(422, UNPROCESSABLE_ENTITY, "error.business.exception"),
    DATA_EXCEPTION(422, UNPROCESSABLE_ENTITY, "error.data.exception"),
    ACCOUNT_LOCKED(423, LOCKED, "error.account.locked"),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.internal.server.error"),
    JDBC_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.jdbc.exception"),
    SQL_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.sql.exception"),
    HIBERNATE_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.hibernate.exception"),
    NULL_POINTER_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.null.pointer.exception"),
    RUNTIME_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.runtime.exception"),
    EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.exception"),
    SERVLET_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "error.servlet.exception"),
    NO_CODE(501, NOT_IMPLEMENTED, "error.code.0");

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    Errors(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
