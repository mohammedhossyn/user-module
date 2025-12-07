package com.usermodule.dto.user.validation;

import com.usermodule.utils.ResourceBundleUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private final ResourceBundleUtil resourceBundleUtil;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String passwordRegexLevel = "0";
        boolean isValid = false;
        if (passwordRegexLevel == null || passwordRegexLevel.isEmpty()) {
            isValid = true;
        } else {
            String passwordRegex = null;
            switch (passwordRegexLevel) {
                case "1" -> passwordRegex = "^(?=.*[a-z]).{6,}$";
                case "2" -> passwordRegex = "^(?=.*[a-z])(?=.*[A-Z]).{6,}$";
                case "3" -> passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,}$";
                case "4" -> passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,}$";
                case "5" -> passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$";
                default -> isValid = true;
            }
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(resourceBundleUtil.getMessage("password.validation.level" +
                                passwordRegexLevel))
                        .addConstraintViolation();
                Pattern pattern = Pattern.compile(passwordRegex);
                Matcher matcher = pattern.matcher(value);
                isValid = matcher.matches();
            }
        }
        return isValid;
    }
}