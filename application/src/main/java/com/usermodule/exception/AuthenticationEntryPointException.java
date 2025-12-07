package com.usermodule.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.usermodule.exception.CodeException.Auth;

@Component("delegatedAuthEntryPoint")
@RequiredArgsConstructor
public class AuthenticationEntryPointException implements AuthenticationEntryPoint {

    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        Exception exception = authException;
        if (request.getAttribute("businessExceptionMsg") != null &&
                request.getAttribute("businessExceptionMsg") instanceof String message) {
            if (message.contains("The Token has expired on")) {
                exception = new BusinessException(Auth.TOKEN_EXPIRE_DATE);
            } else if (message.contains("The Token's Signature resulted invalid")) {
                exception = new BusinessException(Auth.TOKEN_SIGNATURE_INVALID);
            } else if (message.contains("The provided Algorithm doesn't match the one defined in the JWT's Header")) {
                exception = new BusinessException(Auth.TOKEN_ALGORITHM_DOESNT_MATCH);
            } else if (message.contains("The Token is Null")) {
                exception = new BusinessException(Auth.TOKEN_NOT_FOUND);
            } else {
                exception = new BusinessException(message);
            }
        }
        handlerExceptionResolver.resolveException(request, response, null, exception);
    }
}
