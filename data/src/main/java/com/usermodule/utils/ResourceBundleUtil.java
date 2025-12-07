package com.usermodule.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ResourceBundleUtil {

    @Value("${resourceBundle.defaultLocale}")
    private String defaultLocale;

    private final ResourceBundleMessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, "", new Locale(defaultLocale));
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, "", new Locale(defaultLocale));
    }
}
