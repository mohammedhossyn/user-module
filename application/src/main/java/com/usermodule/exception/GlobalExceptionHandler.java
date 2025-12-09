package com.usermodule.exception;


import com.usermodule.dto.common.ApiErrorResponseDTO;
import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.utils.BusinessExceptionUtil;
import com.usermodule.utils.DataExceptionUtil;
import com.usermodule.utils.ResourceBundleUtil;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.usermodule.exception.Errors.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResourceBundleUtil resourceBundleUtil;
    private final ApiResponseInspector apiResponseInspector;
    private final BusinessExceptionUtil businessExceptionUtil;
    private final DataExceptionUtil dataExceptionUtil;

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponseDTO> handleLockedException(LockedException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("lockedException")
                .code(ACCOUNT_LOCKED.getCode())
                .message(resourceBundleUtil.getMessage(ACCOUNT_LOCKED.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(ACCOUNT_LOCKED.getDescription()));
        return ResponseEntity
                .status(ACCOUNT_LOCKED.getHttpStatus())
                .body(appResponseDto);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponseDTO> handleDisabledException(DisabledException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("disabledException")
                .code(ACCOUNT_DISABLED.getCode())
                .message(resourceBundleUtil.getMessage(ACCOUNT_DISABLED.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(ACCOUNT_DISABLED.getDescription()));
        return ResponseEntity
                .status(ACCOUNT_DISABLED.getHttpStatus())
                .body(appResponseDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDTO> handleBadCredentialsException(BadCredentialsException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("badCredentialsException")
                .code(BAD_CREDENTIALS.getCode())
                .message(resourceBundleUtil.getMessage(BAD_CREDENTIALS.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(BAD_CREDENTIALS.getDescription()));
        return ResponseEntity
                .status(BAD_CREDENTIALS.getHttpStatus())
                .body(appResponseDto);
    }

//    @ExceptionHandler(MessagingException.class)
//    public ResponseEntity<ApiResponseDTO> handleMessagingException(MessagingException exp){
//        exp.printStackTrace();
//        List<ApiErrorResponseDTO> errors = new ArrayList<>();
//        errors.add(ApiErrorResponseDTO.builder()
//                .title("messagingException")
//                .code(INTERNAL_SERVER_ERROR.getCode())
//                .message(resourceBundleUtil.getMessage(INTERNAL_SERVER_ERROR.getDescription()))
//                .className(exp.getStackTrace()[0].getClassName())
//                .methodName(exp.getStackTrace()[0].getMethodName())
//                .line(exp.getStackTrace()[0].getLineNumber())
//                .build());
//        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
//        resourceBundleUtil.getMessage(INTERNAL_SERVER_ERROR.getDescription()));
//        return ResponseEntity
//                .status(INTERNAL_SERVER_ERROR.getHttpStatus())
//                .body(appResponseDto);
//    }

    @ExceptionHandler(ActivationTokenException.class)
    public ResponseEntity<ApiResponseDTO> handleActivationTokenException(ActivationTokenException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("activationTokenException")
                .code(ACTIVATION_TOKEN_EXCEPTION.getCode())
                .message(resourceBundleUtil.getMessage(ACTIVATION_TOKEN_EXCEPTION.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(exp.getMessage()));
        return ResponseEntity
                .status(ACTIVATION_TOKEN_EXCEPTION.getHttpStatus())
                .body(appResponseDto);
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ApiResponseDTO> handleOperationNotPermittedException(OperationNotPermittedException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("operationNotPermittedException")
                .code(OPERATION_NOT_PERMITTED_EXCEPTION.getCode())
                .message(resourceBundleUtil.getMessage(OPERATION_NOT_PERMITTED_EXCEPTION.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(exp.getMessage()));
        return ResponseEntity
                .status(OPERATION_NOT_PERMITTED_EXCEPTION.getHttpStatus())
                .body(appResponseDto);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO> handleAlreadyExistsException(AlreadyExistsException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("alreadyExistsException")
                .code(ALREADY_EXISTS_EXCEPTION.getCode())
                .message(resourceBundleUtil.getMessage(ALREADY_EXISTS_EXCEPTION.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(exp.getMessage()));
        return ResponseEntity
                .status(ALREADY_EXISTS_EXCEPTION.getHttpStatus())
                .body(appResponseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleException(Exception exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("Exception")
                .code(EXCEPTION.getCode())
                .message(resourceBundleUtil.getMessage(EXCEPTION.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(EXCEPTION.getDescription()));
        return ResponseEntity
                .status(EXCEPTION.getHttpStatus())
                .body(appResponseDto);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ApiResponseDTO> handleNotFoundException(NotFound exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("notFound")
                .code(PAGE_NOT_FOUND.getCode())
                .message(resourceBundleUtil.getMessage(PAGE_NOT_FOUND.getDescription()))
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(PAGE_NOT_FOUND.getDescription()));
        return ResponseEntity
                .status(PAGE_NOT_FOUND.getHttpStatus())
                .body(appResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        exp.getBindingResult().getAllErrors().forEach(error ->
                errors.add(ApiErrorResponseDTO.builder()
                        .title("methodArgumentNotValidException")
                        .description(error.getCode())
                        .name(error.getObjectName())
                        .code(METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getCode())
                        .field(((FieldError) error).getField())
                        .value(((FieldError) error).getRejectedValue())
                        .message(resourceBundleUtil.getMessage(METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getDescription()) + "\n" +
                                Objects.requireNonNull(error.getDefaultMessage())
                                        .replace("{0}", "{" + ((FieldError) error).getField() + "}"))
                        .className(exp.getStackTrace()[0].getClassName())
                        .methodName(exp.getStackTrace()[0].getMethodName())
                        .line(exp.getStackTrace()[0].getLineNumber())
                        .build()));
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDTO> handleAuthenticationException(AuthenticationException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleAuthenticationException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(AUTHENTICATION_EXCEPTION.getDescription()))
                .name("authentication")
                .code(AUTHENTICATION_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(AUTHENTICATION_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(AUTHENTICATION_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiResponseDTO> handleInsufficientAuthenticationException(InsufficientAuthenticationException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("insufficientAuthenticationException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(resourceBundleUtil.getMessage(INSUFFICIENT_AUTHENTICATION_EXCEPTION.getDescription())))
                .name("insufficientAuthenticationException")
                .code(INSUFFICIENT_AUTHENTICATION_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(INSUFFICIENT_AUTHENTICATION_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO> handleAccessDeniedException(AccessDeniedException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("accessDeniedException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(resourceBundleUtil.getMessage(ACCESS_DENIED_EXCEPTION.getDescription())))
                .name("access denied")
                .code(ACCESS_DENIED_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(ACCESS_DENIED_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(ACCESS_DENIED_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO> handleIllegalArgumentException(IllegalArgumentException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleIllegalArgumentException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(resourceBundleUtil.getMessage(ILLEGAL_ARGUMENT_EXCEPTION.getDescription())))
                .name("illegal argument")
                .code(ILLEGAL_ARGUMENT_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(ILLEGAL_ARGUMENT_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(ILLEGAL_ARGUMENT_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponseDTO> handleNullPointerException(NullPointerException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleNullPointerException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(NULL_POINTER_EXCEPTION.getDescription()))
                .name("null pointer")
                .code(NULL_POINTER_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(NULL_POINTER_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(NULL_POINTER_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleNoHandlerFoundException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(NO_HANDLER_FOUND_EXCEPTION.getDescription()))
                .name("no Handler found")
                .code(NO_HANDLER_FOUND_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(NO_HANDLER_FOUND_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(NO_HANDLER_FOUND_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDTO> handleBusinessException(BusinessException exp) {
        if (exp.getException() != null) {
            exp.getException().printStackTrace();

            if (exp.getException() instanceof BusinessException) {
                exp = (BusinessException) exp.getException();
            } else {
                if (exp.getDescription() == null) {
                    exp.setDescription(exp.getException().getMessage());
                } else {
                    exp.setDescription(exp.getDescription() + " - " + exp.getException().getMessage());
                }
            }
        } else {
            exp.printStackTrace();
        }
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("businessException")
                .description(exp.getDescription())
                .message(resourceBundleUtil.getMessage(BUSINESS_EXCEPTION.getDescription()) + "\n" +
                        businessExceptionUtil.getMessage(exp))
                .name(exp.getName())
                .code(BUSINESS_EXCEPTION.getCode())
                .field(exp.getField())
                .value(exp.getValue())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(BUSINESS_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(BUSINESS_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ApiResponseDTO> handleDataException(DataException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("dataException")
                .description(exp.getDescription())
                .message(resourceBundleUtil.getMessage(DATA_EXCEPTION.getDescription()) + "\n" +
                        dataExceptionUtil.getMessage(exp))
                .name(exp.getName())
                .code(DATA_EXCEPTION.getCode())
                .field(exp.getField())
                .value(exp.getValue())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(DATA_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(DATA_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO> handleConstraintViolationException(ConstraintViolationException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("constraintViolationException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(CONSTRAINT_VIOLATION_EXCEPTION.getDescription()))
                .name("constraint violation")
                .code(CONSTRAINT_VIOLATION_EXCEPTION.getCode())
                .field(exp.getConstraintName())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(CONSTRAINT_VIOLATION_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(CONSTRAINT_VIOLATION_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO> handleValidationConstraintViolationException(
            jakarta.validation.ConstraintViolationException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        for (ConstraintViolation<?> error : exp.getConstraintViolations()) {
            errors.add(ApiErrorResponseDTO.builder()
                    .title(error.getMessage())
                    .description(error.getConstraintDescriptor().getMessageTemplate())
                    .name("validationConstraintViolationException")
                    .code(CONSTRAINT_VIOLATION_EXCEPTION.getCode())
                    .field(Objects.requireNonNull(error.getPropertyPath()).toString())
                    .value(error.getInvalidValue())
                    .message(resourceBundleUtil.getMessage(CONSTRAINT_VIOLATION_EXCEPTION.getDescription()))
                    .className(exp.getStackTrace()[0].getClassName())
                    .methodName(exp.getStackTrace()[0].getMethodName())
                    .line(exp.getStackTrace()[0].getLineNumber())
                    .build());
        }
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(CONSTRAINT_VIOLATION_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(CONSTRAINT_VIOLATION_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(JDBCException.class)
    public ResponseEntity<ApiResponseDTO> handleJDBCException(JDBCException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleJDBCException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(JDBC_EXCEPTION.getDescription()))
                .name(exp.getSQL())
                .code(JDBC_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(JDBC_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(JDBC_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleSQLIntegrityConstraintViolationException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION.getDescription()))
                .name("sql integrity constraint violation")
                .code(SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponseDTO> handleSQException(SQLException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleSQException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(SQL_EXCEPTION.getDescription()))
                .name("sql")
                .code(SQL_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(SQL_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(SQL_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<ApiResponseDTO> handleHibernateException(HibernateException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleHibernateException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(HIBERNATE_EXCEPTION.getDescription()))
                .name("hibernate")
                .code(HIBERNATE_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(HIBERNATE_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(HIBERNATE_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleDataIntegrityViolationException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(DATA_INTEGRITY_VIOLATION_EXCEPTION.getDescription()))
                .code(DATA_INTEGRITY_VIOLATION_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());

        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(DATA_INTEGRITY_VIOLATION_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(DATA_INTEGRITY_VIOLATION_EXCEPTION.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO> handleRuntimeException(RuntimeException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleRuntimeException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(RUNTIME_EXCEPTION.getDescription()))
                .code(RUNTIME_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(RUNTIME_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(RUNTIME_EXCEPTION.getHttpStatus())
                .body(response);
    }


    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ApiResponseDTO> servletException(ServletException exp) {
        exp.printStackTrace();
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleException")
                .description(exp.getMessage())
                .message(resourceBundleUtil.getMessage(SERVLET_EXCEPTION.getDescription()))
                .code(SERVLET_EXCEPTION.getCode())
                .className(exp.getStackTrace()[0].getClassName())
                .methodName(exp.getStackTrace()[0].getMethodName())
                .line(exp.getStackTrace()[0].getLineNumber())
                .build());
        var response = apiResponseInspector.apiResponseBuilder(errors,
                resourceBundleUtil.getMessage(SERVLET_EXCEPTION.getDescription()));
        return ResponseEntity
                .status(SERVLET_EXCEPTION.getHttpStatus())
                .body(response);
    }
}
