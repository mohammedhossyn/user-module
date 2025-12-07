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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestException {

    private final BusinessExceptionUtil businessExceptionUtil;
    private final DataExceptionUtil dataExceptionUtil;
    private final ResourceBundleUtil resourceBundleUtil;
    private final ApiResponseInspector apiResponseInspector;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.add(ApiErrorResponseDTO.builder()
                        .title("handleValidationExceptions")
                        .description(error.getCode())
                        .name(error.getObjectName())
                        .code(413)
                        .field(((FieldError) error).getField())
                        .value(((FieldError) error).getRejectedValue())
                        .message(Objects.requireNonNull(error.getDefaultMessage())
                                .replace("{0}", "{" + ((FieldError) error).getField() + "}"))
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .className(ex.getStackTrace()[0].getClassName())
                        .methodName(ex.getStackTrace()[0].getMethodName())
                        .line(ex.getStackTrace()[0].getLineNumber())
                        .build()));
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponseDTO handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleAuthenticationException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.authenticationEntryPoint"))
                .name("authentication")
                .code(414)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ApiResponseDTO handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleInsufficientAuthenticationException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.app.service.not.found"))
                .name("handleInsufficientAuthenticationException")
                .code(502)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ApiResponseDTO handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleAccessDeniedException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.accessDenied"))
                .name("access denied")
                .code(403)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ApiResponseDTO handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleIllegalArgumentException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleIllegalArgumentException"))
                .name("illegal argument")
                .code(405)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({NullPointerException.class})
    public ApiResponseDTO handleNullPointerException(NullPointerException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleNullPointerException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleNullPointerException"))
                .name("null pointer")
                .code(402)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    public ApiResponseDTO handleNoHandlerFoundException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleNoHandlerFoundException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleNoHandlerFoundException"))
                .name("no Handler found")
                .code(406)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponseDTO handleBusinessException(BusinessException ex, WebRequest request) {
        Throwable throwable;
        if (ex.getException() != null) {
            ex.getException().printStackTrace();
            throwable = ex.getException();
            log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
            if (ex.getException() instanceof BusinessException) {
                ex = (BusinessException) ex.getException();
            } else {
                if (ex.getDescription() == null) {
                    ex.setDescription(ex.getException().getMessage());
                } else {
                    ex.setDescription(ex.getDescription() + " - " + ex.getException().getMessage());
                }
            }
        } else {
            ex.printStackTrace();
            throwable = ex;
            log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        }
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleBusinessException")
                .description(ex.getDescription())
                .message(businessExceptionUtil.getMessage(ex))
                .name(ex.getName())
                .code(ex.getCode() != null ? ex.getCode() : 500)
                .field(ex.getField())
                .value(ex.getValue())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, throwable);
    }

    @ExceptionHandler(DataException.class)
    public ApiResponseDTO handleDataException(DataException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleDataException")
                .description(ex.getDescription())
                .message(dataExceptionUtil.getMessage(ex))
                .name(ex.getName())
                .code(ex.getCode())
                .field(ex.getField())
                .value(ex.getValue())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ApiResponseDTO handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleConstraintViolationException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleConstraintViolationException"))
                .name("constraint violation")
                .code(ex.getErrorCode())
                .field(ex.getConstraintName())
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({jakarta.validation.ConstraintViolationException.class})
    public ApiResponseDTO handleValidationConstraintViolationException(jakarta.validation.ConstraintViolationException ex,
                                                                       WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            errors.add(ApiErrorResponseDTO.builder()
                    .title(error.getMessage())
                    .description(error.getConstraintDescriptor().getMessageTemplate())
                    .name("handleValidationConstraintViolationException")
                    .code(407)
                    .field(Objects.requireNonNull(error.getPropertyPath()).toString())
                    .value(error.getInvalidValue())
                    .message(resourceBundleUtil.getMessage(error.getMessageTemplate()))
                    .path(((ServletWebRequest) request).getRequest().getRequestURI())
                    .className(ex.getStackTrace()[0].getClassName())
                    .methodName(ex.getStackTrace()[0].getMethodName())
                    .line(ex.getStackTrace()[0].getLineNumber())
                    .build());
        }
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({JDBCException.class})
    public ApiResponseDTO handleJDBCException(JDBCException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleJDBCException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleJDBCException"))
                .name(ex.getSQL())
                .code(ex.getErrorCode())
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ApiResponseDTO handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex,
                                                                         WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleSQLIntegrityConstraintViolationException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleSQLIntegrityConstraintViolationException"))
                .name("sql integrity constraint violation")
                .code(ex.getErrorCode())
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({SQLException.class})
    public ApiResponseDTO handleSQException(SQLException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleSQException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleSQException"))
                .name("sql")
                .code(ex.getErrorCode())
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({HibernateException.class})
    public ApiResponseDTO handleHibernateException(HibernateException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleHibernateException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleHibernateException"))
                .name("hibernate")
                .code(408)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ApiResponseDTO handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleDataIntegrityViolationException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleDataIntegrityViolationException"))
                .name(null)
                .code(409)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());

        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({RuntimeException.class})
    public ApiResponseDTO handleRuntimeException(RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleRuntimeException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleRuntimeException"))
                .name(null)
                .code(410)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler({Exception.class})
    public ApiResponseDTO handleException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage("err.handleException"))
                .name(null)
                .code(411)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }

    @ExceptionHandler(value = { ServletException.class })
    public ApiResponseDTO servletException(ServletException ex, WebRequest request) {
        ex.printStackTrace();
        log.error(((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .title("handleException")
                .description(ex.getMessage())
                .message(resourceBundleUtil.getMessage(ex.getMessage()))
                .name(null)
                .code(412)
                .field(null)
                .value(null)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .className(ex.getStackTrace()[0].getClassName())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .line(ex.getStackTrace()[0].getLineNumber())
                .build());
        return apiResponseInspector.apiErrorResponseBuilder(errors, ex);
    }
}
