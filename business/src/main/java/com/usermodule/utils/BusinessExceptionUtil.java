package com.usermodule.utils;

import com.usermodule.exception.BusinessException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.usermodule.exception.CodeException.*;

@Component
@RequiredArgsConstructor
public class BusinessExceptionUtil {

    private final ResourceBundleUtil resourceBundleUtil;

    public String getMessage(BusinessException ex) {
        if (ex != null) {
            return getErrorMessage(ex);
        } else {
            return null;
        }
    }

    private String getErrorMessage(@NonNull BusinessException ex) {
        if (ex.getCode() != null) {
            return getMessageWithErrorCode(ex);
        } else if (ex.getMessage() != null) {
            return getDefaultMessage(ex.getMessage());
        } else {
            return null;
        }
    }

    private String getMessageWithErrorCode(@NonNull BusinessException ex) {
        return switch (ex.getCode()) {
            case App.INTERNAL_SYSTEM_ERROR -> resourceBundleUtil.getMessage("err.app.internal.system.error");
            case App.TRANSACTION_IS_OPEN -> resourceBundleUtil.getMessage("err.app.transaction.is.open");
            case App.SERVICE_NOT_FOUND -> resourceBundleUtil.getMessage("err.app.service.not.found");

            case Auth.INVALID_USERNAME_OR_PASSWORD ->
                    resourceBundleUtil.getMessage("err.auth.invalid.username.or.password");
            case Auth.TOKEN_EXPIRE_DATE -> resourceBundleUtil.getMessage("err.auth.token.expire.date");
            case Auth.TOKEN_SIGNATURE_INVALID -> resourceBundleUtil.getMessage("err.auth.token.signature.invalid");
            case Auth.TOKEN_ALGORITHM_DOESNT_MATCH ->
                    resourceBundleUtil.getMessage("err.auth.token.algorithm.doesnt.match");
            case Auth.TOKEN_NOT_FOUND -> resourceBundleUtil.getMessage("err.auth.token.not.found");

            case User.USER_NOT_FOUND -> resourceBundleUtil.getMessage("err.user.not.found");

            case File.FILE_NOT_FOUND -> resourceBundleUtil.getMessage("err.file.not.found");

            default -> getDefaultMessage(ex.getMessage());
        };
    }

    private String getDefaultMessage(String message) {
        String defaultMessage = null;
        if (message != null && !message.isEmpty()) {
            defaultMessage = resourceBundleUtil.getMessage(message);
            if (defaultMessage == null || defaultMessage.isEmpty()) {
                defaultMessage = message;
            }
        }
        return defaultMessage;
    }
}
