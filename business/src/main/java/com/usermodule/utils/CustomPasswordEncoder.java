package com.usermodule.utils;

import com.usermodule.service.security.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoderService passwordEncoderService;

    @Override
    public String encode(CharSequence plainTextPassword) {
        return passwordEncoderService.encode(plainTextPassword.toString(), KeyGenerator.getKey());
    }

    @Override
    public boolean matches(CharSequence plainTextPassword, String passwordInDatabase) {
        return passwordEncoderService.encode(plainTextPassword.toString(), KeyGenerator.getKey()).equals(passwordInDatabase);
    }
}